package com.jjs.ClothingInventorySaleReformPlatform.domain.reform.repository;

import com.jjs.ClothingInventorySaleReformPlatform.domain.reform.entity.estimate.Estimate;
import com.jjs.ClothingInventorySaleReformPlatform.domain.reform.entity.estimate.EstimateStatus;
import com.jjs.ClothingInventorySaleReformPlatform.domain.reform.entity.reformRequest.ReformRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface EstimateRepository extends JpaRepository<Estimate, Long> {
    Optional<Estimate> findEstimateById(Long id);
    Optional<Estimate> findEstimateByRequestNumber(ReformRequest requestNumber);
    Optional<Estimate> findByRequestNumberAndEstimateStatusNot(ReformRequest requestNumber, EstimateStatus status);
}
