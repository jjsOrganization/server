package com.jjs.ClothingInventorySaleReformPlatform.repository;

import com.jjs.ClothingInventorySaleReformPlatform.domain.Designer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DesignerRepository extends JpaRepository<Designer, String> {

    Optional<Designer> findByEmail(String email); // 메소드 커스텀해서 SERVICE계층에서 사용
    Optional<Designer> findByPhoneNumber(String phoneNumber);
    Optional<Designer> findByDesignerName(String designerName);
}
