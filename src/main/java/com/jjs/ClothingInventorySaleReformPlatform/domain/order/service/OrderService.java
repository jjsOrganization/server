package com.jjs.ClothingInventorySaleReformPlatform.domain.order.service;

import com.jjs.ClothingInventorySaleReformPlatform.domain.product.entity.Category;
import com.jjs.ClothingInventorySaleReformPlatform.domain.product.repository.CategoryRepository;
import com.jjs.ClothingInventorySaleReformPlatform.domain.user.entity.PurchaserInfo;
import com.jjs.ClothingInventorySaleReformPlatform.domain.cart.entity.Cart;
import com.jjs.ClothingInventorySaleReformPlatform.domain.order.entity.Order;
import com.jjs.ClothingInventorySaleReformPlatform.domain.order.entity.OrderDetail;
import com.jjs.ClothingInventorySaleReformPlatform.domain.order.entity.OrderStatus;
import com.jjs.ClothingInventorySaleReformPlatform.domain.product.entity.Product;
import com.jjs.ClothingInventorySaleReformPlatform.domain.order.dto.OrderDTO;
import com.jjs.ClothingInventorySaleReformPlatform.domain.order.dto.OrderDetailDTO;
import com.jjs.ClothingInventorySaleReformPlatform.domain.user.repository.PurchaserRepository;
import com.jjs.ClothingInventorySaleReformPlatform.domain.cart.repository.CartRepository;
import com.jjs.ClothingInventorySaleReformPlatform.domain.order.repository.OrderRepository;
import com.jjs.ClothingInventorySaleReformPlatform.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final PurchaserRepository purchaserRepository;
    private final CartRepository cartRepository;
    private final CategoryRepository categoryRepository;

    /**
     * 상품 주문 첫 번째 절차 - db에 상품 정보 저장
     * @param orderDTO
     * @param isCart
     * @return
     */
    @Transactional
    public Long createOrder(OrderDTO orderDTO, boolean isCart) {
        // 인증 정보에서 구매자 이메일 가져오기
        String currentUsername = getAuthentication();

        // 구매자 정보 조회
        PurchaserInfo purchaserInfo = purchaserRepository.findPurchaserByEmail(currentUsername);

        // 사용자의 장바구니를 식별하기 위한 추가 로직 - 장바구니 내의 상품 id로 주문이 진행되야 되는데 현재는 전체 상품 id로 구매가 진행됨.(로직이 잘못됨) -> 추후 수정 필요
        if (isCart) {
            Cart cart = cartRepository.findByPurchaserInfoEmail(currentUsername);
            if (cart == null) {
                throw new IllegalStateException("현재 사용자에 대한 장바구니를 찾을 수 없습니다.");
            }
        }

        // Order 엔티티 생성 - 코드에서 DTO 분리를 유지하고, 필요 시 toEntity 메소드를 통해 DTO에서 엔티티로 변환
        Order saveOrder = orderDTO.toEntity(purchaserInfo);

        // OrderDetail 생성
        for (OrderDetailDTO detail : orderDTO.getOrderDetails()) {
            Product product = productRepository.findById(detail.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("상품 정보를 찾을 수 없습니다."));

            if (detail.getQuantity() > product.getProductStock()) {
                throw new IllegalArgumentException("상품 [" + product.getProductName() + "]의 재고가 부족합니다. 요청 수량: "
                        + detail.getQuantity() + ", 재고: " + product.getProductStock());
            }

            // OrderDetail 엔티티 생성 - 코드에서 DTO 분리를 유지하고, 필요 시 toEntity 메소드를 통해 DTO에서 엔티티로 변환
            OrderDetail orderDetail = detail.toEntity(saveOrder, product);
            saveOrder.addOrderDetail(orderDetail);
        }
        orderRepository.save(saveOrder);
        return saveOrder.getOrderId();
    }

    /**
     * 상품 주문 두 번째 절차 - db에 회원 주문 정보(개인 정보) 저장
     * @param purchaserEmail
     * @param orderDTO
     */
    @Transactional
    public void updateOrder(String purchaserEmail, OrderDTO orderDTO) {

        // 현재 로그인한 사용자(구매자)의 가장 최근 주문을 조회
        Order order = orderRepository.findTopByPurchaserInfoEmailOrderByOrderDateDesc(purchaserEmail)
                        .orElseThrow(() -> new IllegalArgumentException("주문 정보를 찾을 수 없습니다."));

        order.updateOrderDetails(orderDTO);

        // 총 가격 계산
        int totalPrice = order.calculateTotalPrice();
        order.setTotalPrice(totalPrice);
        orderRepository.save(order);

        // 집계된 데이터를 카테고리 테이블에 업데이트
        updateCategoryCounts(order.calculateCategoryCounts());
    }



    /**
     * 상품을 구매하면 구매한 상품의 개수만큼 상품의 재고를 감소시켜야됨.
     *
     */
    @Transactional
    public void decreaseProductStock(String purchaserEmail) {
        /*
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("주문 정보를 찾을 수 없습니다."));
        */

        // 현재 로그인한 사용자(구매자)의 가장 최근 주문을 조회
        Order order = orderRepository.findTopByPurchaserInfoEmailOrderByOrderDateDesc(purchaserEmail)
                .orElseThrow(() -> new IllegalArgumentException("주문 정보를 찾을 수 없습니다."));

        for (OrderDetail detail : order.getOrderDetails()) {
            Product product = detail.getProduct();
            int purchaseQuantity = detail.getQuantity();

            // 현재 재고에서 수량 차감
            int newStock = product.getProductStock() - purchaseQuantity;
            if (newStock < 0) {
                throw new IllegalStateException("상품의 재고가 부족합니다. 상품 ID: " + product.getId());
            }
            product.setProductStock(newStock);  // 업데이트된 재고 수량 설정
            productRepository.save(product);  // 변경된 상품 정보를 저장
        }
    }

    /**
     * 상품에 해당되는 카테고리의 집계 update
     * @param categoryCounts
     */
    private void updateCategoryCounts(Map<Category, Long> categoryCounts) {
        categoryCounts.forEach((category, count) -> {
            Category loadedCategory = categoryRepository.findById(category.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Category not found"));
            loadedCategory.incrementCompletedProductCount(count);  // 상품 주문 완료 시, 상품에 해당되는 카테고리 수 증가
            categoryRepository.save(loadedCategory);
        });
    }

    private static String getAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        return currentUsername;
    }
}
