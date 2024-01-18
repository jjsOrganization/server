package com.jjs.ClothingInventorySaleReformPlatform.repository;

import com.jjs.ClothingInventorySaleReformPlatform.domain.PurchaserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaserRepository extends JpaRepository<PurchaserInfo, String> {
}
