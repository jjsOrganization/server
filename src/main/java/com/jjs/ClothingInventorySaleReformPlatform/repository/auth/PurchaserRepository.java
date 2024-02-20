package com.jjs.ClothingInventorySaleReformPlatform.repository.auth;

import com.jjs.ClothingInventorySaleReformPlatform.domain.user.PurchaserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaserRepository extends JpaRepository<PurchaserInfo, String> {
    PurchaserInfo findPurchaserByEmail(String email);
}
