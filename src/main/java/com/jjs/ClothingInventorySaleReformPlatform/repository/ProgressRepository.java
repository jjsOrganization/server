package com.jjs.ClothingInventorySaleReformPlatform.repository;

import com.jjs.ClothingInventorySaleReformPlatform.domain.reform.Progressmanagement;
import com.jjs.ClothingInventorySaleReformPlatform.domain.reform.estimate.Estimate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProgressRepository extends JpaRepository<Progressmanagement, Long> {

    Optional<Progressmanagement> findByEstimateNumber_Id(Long estimateNumber);  // EstimateNumber 필드의 id 속성을 사용하여 ProgressManagement 엔티티를 조회
    Optional<Progressmanagement> findByEstimateNumber(Estimate estimate);  // Estimate 엔티티의 인스턴스를 먼저 조회한 다음, 이 인스턴스를 사용하여 ProgressManagement를 조회


}
