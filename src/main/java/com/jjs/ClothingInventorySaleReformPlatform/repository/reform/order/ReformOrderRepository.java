package com.jjs.ClothingInventorySaleReformPlatform.repository.reform.order;

import com.jjs.ClothingInventorySaleReformPlatform.domain.reform.estimate.Estimate;
import com.jjs.ClothingInventorySaleReformPlatform.domain.reform.order.ReformOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReformOrderRepository extends JpaRepository<ReformOrder, Long> {

    Optional<ReformOrder> findReformOrderByEstimateId(Long id);
}
