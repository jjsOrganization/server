package com.jjs.ClothingInventorySaleReformPlatform.repository.reform.estimate;

import com.jjs.ClothingInventorySaleReformPlatform.domain.reform.estimate.EstimateImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EstimateImgRepository extends JpaRepository<EstimateImage, Long> {

    List<EstimateImage> findAllByEstimateId(Long estimateNumber);
    public void deleteByEstimateId(Long estimateNumber);
}
