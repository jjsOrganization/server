package com.jjs.ClothingInventorySaleReformPlatform.repository.order;

import com.jjs.ClothingInventorySaleReformPlatform.domain.order.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
}
