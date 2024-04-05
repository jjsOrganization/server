package com.jjs.ClothingInventorySaleReformPlatform.repository.reformrequest;


import com.jjs.ClothingInventorySaleReformPlatform.domain.reform.reformrequest.ReformRequest;
import com.jjs.ClothingInventorySaleReformPlatform.domain.user.DesignerInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReformRequestRepository extends JpaRepository<ReformRequest, Object> {
    Optional<ReformRequest> findReformRequestById(Long id);
    Optional<List<ReformRequest>> findReformRequestsByDesignerEmail(DesignerInfo designerEmail);
    //Optional<List<ReformRequest>> findReformRequestsByClientEmail(PurchaserInfo purchaserEmail);
    List<ReformRequest> findByClientEmail_Email(String email);
    //Optional<ReformRequest> findClientAndDesignerEmailsById(Long id);
    ReformRequest findAllById(Long id);
}
