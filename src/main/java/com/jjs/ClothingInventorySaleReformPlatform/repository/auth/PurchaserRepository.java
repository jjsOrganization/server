package com.jjs.ClothingInventorySaleReformPlatform.repository.auth;

import com.jjs.ClothingInventorySaleReformPlatform.domain.user.PurchaserInfo;
import com.jjs.ClothingInventorySaleReformPlatform.domain.user.SellerInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PurchaserRepository extends JpaRepository<PurchaserInfo, String> {
    PurchaserInfo findPurchaserByEmail(String email);
    Optional<PurchaserInfo> findByEmail (String email);
}
