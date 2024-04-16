package com.jjs.ClothingInventorySaleReformPlatform.domain.reform.repository;


import com.jjs.ClothingInventorySaleReformPlatform.domain.reform.entity.reformRequest.ReformRequest;
import com.jjs.ClothingInventorySaleReformPlatform.domain.user.entity.DesignerInfo;
import com.jjs.ClothingInventorySaleReformPlatform.domain.user.entity.PurchaserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReformRequestRepository extends JpaRepository<ReformRequest, Object> {
    Optional<ReformRequest> findReformRequestById(Long id);
    Optional<List<ReformRequest>> findReformRequestsByDesignerEmail(DesignerInfo designerEmail);
    List<ReformRequest> findReformRequestsByPurchaserEmail(PurchaserInfo purchaserEmail);
    ReformRequest findAllById(Long id);
}
