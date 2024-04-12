package com.jjs.ClothingInventorySaleReformPlatform.domain.reform.repository;

import com.jjs.ClothingInventorySaleReformPlatform.domain.reform.entity.ReformRequestImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReformRequestImgRepository extends JpaRepository<ReformRequestImage, Long> {

    public void deleteByReformRequestId(Long requestNumber);

    List<ReformRequestImage> findAllByReformRequestId(Long requestNumber);
}
