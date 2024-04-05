package com.jjs.ClothingInventorySaleReformPlatform.repository.estimate;

import com.jjs.ClothingInventorySaleReformPlatform.domain.reform.estimate.Estimate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EstimateRepository extends JpaRepository<Estimate, Long> {
    Optional<Estimate> findEstimateById(Long id);
}
