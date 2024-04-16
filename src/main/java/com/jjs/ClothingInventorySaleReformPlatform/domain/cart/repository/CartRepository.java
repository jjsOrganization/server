package com.jjs.ClothingInventorySaleReformPlatform.domain.cart.repository;

import com.jjs.ClothingInventorySaleReformPlatform.domain.cart.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {

    Cart findByPurchaserInfoEmail(String purchaserInfoEmail);
}
