package com.jjs.ClothingInventorySaleReformPlatform.repository.cart;

import com.jjs.ClothingInventorySaleReformPlatform.domain.cart.CartProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartProductRepository extends JpaRepository<CartProduct, Long> {

    CartProduct findByCartIdAndProductId(long cartId, long productId);
    List<CartProduct> findByCartId(Long cartId);
    Optional<CartProduct> deleteByCartIdAndProductId(long cartId, long productId);

}
