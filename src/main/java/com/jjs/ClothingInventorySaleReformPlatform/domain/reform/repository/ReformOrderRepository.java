package com.jjs.ClothingInventorySaleReformPlatform.domain.reform.repository;

import com.jjs.ClothingInventorySaleReformPlatform.domain.reform.entity.reformOrder.ReformOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReformOrderRepository extends JpaRepository<ReformOrder, Long> {

    Optional<ReformOrder> findReformOrderByEstimateId(Long id);
}
