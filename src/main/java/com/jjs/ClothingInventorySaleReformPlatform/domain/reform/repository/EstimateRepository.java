package com.jjs.ClothingInventorySaleReformPlatform.domain.reform.repository;

import com.jjs.ClothingInventorySaleReformPlatform.domain.reform.entity.Estimate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EstimateRepository extends JpaRepository<Estimate, Long> {
    Optional<Estimate> findEstimateById(Long id);
}
