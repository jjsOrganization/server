package com.jjs.ClothingInventorySaleReformPlatform.repository.estimate;

import com.jjs.ClothingInventorySaleReformPlatform.domain.estimate.EstimateImage;
import com.jjs.ClothingInventorySaleReformPlatform.domain.reformrequest.ReformRequestImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EstimateImgRepository extends JpaRepository<EstimateImage, Long> {

    List<EstimateImage> findAllByEstimateId(Long estimateNumber);
    public void deleteByEstimateId(Long estimateNumber);
}
