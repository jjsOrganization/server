package com.jjs.ClothingInventorySaleReformPlatform.repository.order;


import com.jjs.ClothingInventorySaleReformPlatform.domain.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findTopByPurchaserInfoEmailOrderByOrderDateDesc(String email);  // 구매자 이메일에 대해 가장 최근의 주문을 찾아 반환
}
