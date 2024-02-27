package com.jjs.ClothingInventorySaleReformPlatform.repository.reformrequest;


import com.jjs.ClothingInventorySaleReformPlatform.domain.Estimate;
import com.jjs.ClothingInventorySaleReformPlatform.domain.reformrequest.ReformRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReformRequestRepository extends JpaRepository<ReformRequest, Object> {
    Optional<ReformRequest> findReformRequestById(Long id);
}
