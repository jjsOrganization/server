package com.jjs.ClothingInventorySaleReformPlatform.repository.order;

import com.jjs.ClothingInventorySaleReformPlatform.domain.delivery.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
}
