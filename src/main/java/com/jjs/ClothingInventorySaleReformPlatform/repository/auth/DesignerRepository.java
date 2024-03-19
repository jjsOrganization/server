package com.jjs.ClothingInventorySaleReformPlatform.repository.auth;

import com.jjs.ClothingInventorySaleReformPlatform.domain.user.DesignerInfo;
import com.jjs.ClothingInventorySaleReformPlatform.domain.user.PurchaserInfo;
import com.jjs.ClothingInventorySaleReformPlatform.domain.user.SellerInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DesignerRepository extends JpaRepository<DesignerInfo, String> {
    DesignerInfo findDesignerByEmail(String email);
    Optional<DesignerInfo> findByEmail (String email);

}
