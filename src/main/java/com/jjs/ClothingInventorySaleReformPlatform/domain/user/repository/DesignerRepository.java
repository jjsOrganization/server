package com.jjs.ClothingInventorySaleReformPlatform.domain.user.repository;

import com.jjs.ClothingInventorySaleReformPlatform.domain.user.entity.DesignerInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DesignerRepository extends JpaRepository<DesignerInfo, String> {
    DesignerInfo findDesignerByEmail(String email);
    Optional<DesignerInfo> findByEmail (String email);

}
