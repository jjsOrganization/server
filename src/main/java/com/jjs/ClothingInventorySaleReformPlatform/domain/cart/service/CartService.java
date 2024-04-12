package com.jjs.ClothingInventorySaleReformPlatform.domain.cart.service;

import com.jjs.ClothingInventorySaleReformPlatform.domain.user.entity.PurchaserInfo;
import com.jjs.ClothingInventorySaleReformPlatform.domain.cart.entity.Cart;
import com.jjs.ClothingInventorySaleReformPlatform.domain.cart.entity.CartProduct;
import com.jjs.ClothingInventorySaleReformPlatform.domain.product.entity.Product;
import com.jjs.ClothingInventorySaleReformPlatform.domain.cart.dto.ProductInfoDTO;
import com.jjs.ClothingInventorySaleReformPlatform.domain.cart.repository.CartProductRepository;
import com.jjs.ClothingInventorySaleReformPlatform.domain.cart.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartService {

    private final CartRepository cartRepository;
    private final CartProductRepository cartProductRepository;

    public void createCart(PurchaserInfo purchaserInfo) {
        Cart cart = Cart.createCart(purchaserInfo);
        cartRepository.save(cart);
    }

    // 장바구니에 상품 추가
    @Transactional
    public void addCart(PurchaserInfo purchaserInfo, Product product, int count) {
        Cart cart = cartRepository.findByPurchaserInfoEmail(purchaserInfo.getEmail());
        // Cart가 비어있다면 새로 생성
        if(cart == null){
            cart = Cart.createCart(purchaserInfo);
            cartRepository.save(cart);
        }

        // 상품 재고 수량 확인
        if (product.getProductStock() < count) {
            throw new IllegalArgumentException("요청한 수량은 재고를 초과합니다.");
        }

        // CartProduct 생성
        CartProduct cartProduct = cartProductRepository.findByCartIdAndProductId(cart.getId(),product.getId());

        // cart_item이 비어있다면 새로 생성
        if(cartProduct == null){
            cartProduct = CartProduct.createCartProduct(cart, product, count);
            cartProductRepository.save(cartProduct);
            cart.setCount(cart.getCount() + 1);
        }else{
            // 추가하려는 총 수량이 재고를 초과하는지 확인
            int totalRequested = cartProduct.getCount() + count;
            if (product.getProductStock() < totalRequested) {
                throw new IllegalArgumentException("총 요청 수량이 재고를 초과합니다.");
            }
            // 비어있지 않다면 그만큼 갯수를 추가.
            cartProduct.addCount(count);
            log.info("cartProduct count : " + cartProduct.getCount());
            cartProductRepository.save(cartProduct);
        }
    }

    // 장바구니에 상품 조회
    public List<ProductInfoDTO> userCartView(Cart cart) {
        List<CartProduct> cartProducts = cartProductRepository.findByCartId(cart.getId());
        return cartProducts.stream()
                .map(ProductInfoDTO::from)
                .collect(Collectors.toList());
    }

    // 장바구니에 상품 삭제
    @Transactional
    public void cartProductDelete(long productId){

        // 현재 로그인한 사용자의 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        // 사용자의 장바구니를 식별하기 위한 추가 로직
        Cart cart = cartRepository.findByPurchaserInfoEmail(currentUsername);
        if (cart == null) {
            throw new IllegalStateException("현재 사용자에 대한 장바구니를 찾을 수 없습니다.");
        }

        // 장바구니에 해당 상품이 존재하는지 확인
        Optional<CartProduct> cartProductOptional = Optional.ofNullable(cartProductRepository.findByCartIdAndProductId(cart.getId(), productId));
        if (cartProductOptional.isEmpty()) {
            throw new IllegalArgumentException("장바구니에 제품이 없습니다.");
        }

        // 장바구니에서 productId에 해당하는 상품 삭제
        cartProductRepository.deleteByCartIdAndProductId(cart.getId(), productId);

    }

}
