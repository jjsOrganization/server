package com.jjs.ClothingInventorySaleReformPlatform.repository.reformrequest;

import com.jjs.ClothingInventorySaleReformPlatform.domain.reform.reformrequest.ReformRequestImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReformRequestImgRepository extends JpaRepository<ReformRequestImage, Long> {

    public void deleteByReformRequestId(Long requestNumber);

    List<ReformRequestImage> findAllByReformRequestId(Long requestNumber);
}
