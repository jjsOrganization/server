package com.jjs.ClothingInventorySaleReformPlatform.domain.user.repository;

import com.jjs.ClothingInventorySaleReformPlatform.domain.user.entity.PurchaserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PurchaserRepository extends JpaRepository<PurchaserInfo, String> {
    PurchaserInfo findPurchaserByEmail(String email);
    Optional<PurchaserInfo> findByEmail (String email);
}
