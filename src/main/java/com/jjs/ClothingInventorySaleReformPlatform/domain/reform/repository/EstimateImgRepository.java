package com.jjs.ClothingInventorySaleReformPlatform.domain.reform.repository;

import com.jjs.ClothingInventorySaleReformPlatform.domain.reform.entity.estimate.EstimateImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EstimateImgRepository extends JpaRepository<EstimateImage, Long> {

    List<EstimateImage> findAllByEstimateId(Long estimateNumber);
    public void deleteByEstimateId(Long estimateNumber);
}
