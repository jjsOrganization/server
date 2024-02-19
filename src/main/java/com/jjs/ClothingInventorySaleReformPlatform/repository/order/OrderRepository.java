package com.jjs.ClothingInventorySaleReformPlatform.repository.order;


import com.jjs.ClothingInventorySaleReformPlatform.domain.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
