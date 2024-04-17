package com.jjs.ClothingInventorySaleReformPlatform.domain.cart.repository;

import com.jjs.ClothingInventorySaleReformPlatform.domain.cart.entity.CartProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartProductRepository extends JpaRepository<CartProduct, Long> {

    CartProduct findByCartIdAndProductId(long cartId, long productId);
    List<CartProduct> findByCartId(Long cartId);
    Optional<CartProduct> deleteByCartIdAndProductId(long cartId, long productId);

    // 삭제된 상품은 조회되지 않음
    List<CartProduct> findByCartIdAndProductIsDeletedFalse(Long cartId);

}
