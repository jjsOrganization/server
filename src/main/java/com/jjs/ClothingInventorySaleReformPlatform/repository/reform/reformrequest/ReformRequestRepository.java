package com.jjs.ClothingInventorySaleReformPlatform.repository.reform.reformrequest;


import com.jjs.ClothingInventorySaleReformPlatform.domain.reform.reformrequest.ReformRequest;
import com.jjs.ClothingInventorySaleReformPlatform.domain.user.DesignerInfo;
import com.jjs.ClothingInventorySaleReformPlatform.domain.user.PurchaserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReformRequestRepository extends JpaRepository<ReformRequest, Object> {
    Optional<ReformRequest> findReformRequestById(Long id);
    Optional<List<ReformRequest>> findReformRequestsByDesignerEmail(DesignerInfo designerEmail);
    List<ReformRequest> findReformRequestsByPurchaserEmail(PurchaserInfo purchaserEmail);
    ReformRequest findAllById(Long id);
}
