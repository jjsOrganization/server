package com.jjs.ClothingInventorySaleReformPlatform.repository.auth;

import com.jjs.ClothingInventorySaleReformPlatform.domain.DesignerInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DesignerRepository extends JpaRepository<DesignerInfo, String> {
}
