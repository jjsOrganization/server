package com.jjs.ClothingInventorySaleReformPlatform.repository.designer;

import com.jjs.ClothingInventorySaleReformPlatform.domain.Portfolio;
import com.jjs.ClothingInventorySaleReformPlatform.domain.User;
import jakarta.validation.Valid;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {

    Optional<Portfolio> findByDesignerEmail(User designerEmail); // 이메일로 조회하는 메소드 선언
}
