package com.jjs.ClothingInventorySaleReformPlatform.repository.auth;

import com.jjs.ClothingInventorySaleReformPlatform.domain.user.DesignerInfo;
import com.jjs.ClothingInventorySaleReformPlatform.domain.user.PurchaserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DesignerRepository extends JpaRepository<DesignerInfo, String> {
    DesignerInfo findDesignerByEmail(String email);

}
