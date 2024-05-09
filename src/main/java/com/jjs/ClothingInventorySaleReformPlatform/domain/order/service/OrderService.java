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
import com.jjs.ClothingInventorySaleReformPlatform.domain.order.repository.OrderDetailRepository;
import com.jjs.ClothingInventorySaleReformPlatform.domain.order.repository.OrderRepository;
import com.jjs.ClothingInventorySaleReformPlatform.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final ProductRepository productRepository;
    private final PurchaserRepository purchaserRepository;
    private final CartRepository cartRepository;
    private final CategoryRepository categoryRepository;

    @Transactional
    public Long createCartOrder(OrderDTO orderDTO) {
        // 인증 정보에서 구매자 이메일 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        // 사용자의 장바구니를 식별하기 위한 추가 로직
        Cart cart = cartRepository.findByPurchaserInfoEmail(currentUsername);
        if (cart == null) {
            throw new IllegalStateException("현재 사용자에 대한 장바구니를 찾을 수 없습니다.");
        }

        // 구매자 정보 조회
        PurchaserInfo purchaserInfo = purchaserRepository.findPurchaserByEmail(currentUsername);

        // Order 엔티티 생성
        Order order = new Order();
        order.setPurchaserInfo(purchaserInfo);
        order.setOrderStatus(OrderStatus.ORDER_WAITING);  // 초기 상태
        Order saveOrder = orderRepository.save(order);

        // OrderDetail 생성
        for (OrderDetailDTO detail : orderDTO.getOrderDetails()) {
            Product product = productRepository.findById(detail.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("상품 정보를 찾을 수 없습니다."));

            if (detail.getQuantity() > product.getProductStock()) {
                throw new IllegalArgumentException("상품 [" + product.getProductName() + "]의 재고가 부족합니다. 요청 수량: "
                        + detail.getQuantity() + ", 재고: " + product.getProductStock());
            }

            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(saveOrder);
            orderDetail.setProduct(product);
            orderDetail.setQuantity(detail.getQuantity());
            orderDetail.setPrice(product.getPrice() * detail.getQuantity());
            orderDetailRepository.save(orderDetail);
        }

        return saveOrder.getOrderId();
    }

    @Transactional
    public Long createPageOrder(OrderDTO orderDTO) {
        // 인증 정보에서 구매자 이메일 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        // 구매자 정보 조회
        PurchaserInfo purchaserInfo = purchaserRepository.findPurchaserByEmail(currentUsername);

        // Order 엔티티 생성
        Order order = new Order();
        order.setPurchaserInfo(purchaserInfo);
        order.setOrderStatus(OrderStatus.ORDER_WAITING);  // 초기 상태
        Order saveOrder = orderRepository.save(order);

        // OrderDetail 생성
        for (OrderDetailDTO detail : orderDTO.getOrderDetails()) {
            Product product = productRepository.findById(detail.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("상품 정보를 찾을 수 없습니다."));

            if (detail.getQuantity() > product.getProductStock()) {
                throw new IllegalArgumentException("상품 [" + product.getProductName() + "]의 재고가 부족합니다. 요청 수량: "
                        + detail.getQuantity() + ", 재고: " + product.getProductStock());
            }

            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(saveOrder);
            orderDetail.setProduct(product);
            orderDetail.setQuantity(detail.getQuantity());
            orderDetail.setPrice(product.getPrice() * detail.getQuantity());
            orderDetailRepository.save(orderDetail);
        }

        return saveOrder.getOrderId();
    }

    @Transactional
    public void updateOrder(String purchaserEmail, OrderDTO orderDTO) {

        /*
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("주문 정보를 찾을 수 없습니다."));
         */

        // 현재 로그인한 사용자(구매자)의 가장 최근 주문을 조회
        Order order = orderRepository.findTopByPurchaserInfoEmailOrderByOrderDateDesc(purchaserEmail)
                        .orElseThrow(() -> new IllegalArgumentException("주문 정보를 찾을 수 없습니다."));

        order.setPostcode(orderDTO.getPostcode());
        order.setAddress(orderDTO.getAddress());
        order.setDetailAddress(orderDTO.getDetailAddress());
        order.setPhoneNumber(orderDTO.getPhoneNumber());
        order.setDeliveryRequest(orderDTO.getDeliveryRequest());
        order.setOrderStatus(OrderStatus.ORDER_COMPLETE); // 주문 완료 상태로 업데이트
        // 총 가격 계산
        int totalPrice = 0;
        for (OrderDetail detail : order.getOrderDetails()) {
            totalPrice += detail.getPrice();  // detail.getPrice() : 상품 가격 * 수량
        }
        order.setTotalPrice(totalPrice);
        orderRepository.save(order);

        Map<Category, Long> categoryCounts = order.getOrderDetails().stream()
                .collect(Collectors.groupingBy(
                        orderDetail -> orderDetail.getProduct().getCategory(),
                        Collectors.summingLong(orderDetail -> orderDetail.getQuantity()) // 여기에서 각 상품의 개수를 합산합니다.
                ));

        // 집계된 데이터를 카테고리 테이블에 업데이트
        categoryCounts.forEach((category, count) -> {
            Category loadedCategory = categoryRepository.findById(category.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Category not found"));
            loadedCategory.setCompletedProductCount(loadedCategory.getCompletedProductCount() + count);
            categoryRepository.save(loadedCategory);
        });
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
}
