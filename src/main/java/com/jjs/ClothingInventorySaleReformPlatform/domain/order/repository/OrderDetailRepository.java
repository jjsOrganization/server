package com.jjs.ClothingInventorySaleReformPlatform.domain.order.repository;

import com.jjs.ClothingInventorySaleReformPlatform.domain.order.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
}
