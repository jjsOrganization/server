package com.jjs.ClothingInventorySaleReformPlatform.repository.portfolio;

import com.jjs.ClothingInventorySaleReformPlatform.domain.portfolio.Portfolio;
import com.jjs.ClothingInventorySaleReformPlatform.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import com.jjs.ClothingInventorySaleReformPlatform.repository.portfolio.mapping.ImageUrlMapping;

import java.util.List;
import java.util.Optional;


public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {

    Optional<Portfolio> findByDesignerEmail(User designerEmail); // 이메일로 조회하는 메소드 선언

    Optional<ImageUrlMapping> findPortfolioById(Long id); // mapping 인터페이스 사용

    Optional<List<Portfolio>> findByNameContaining(String keyword); // %LIKE 검색(DB)

    Optional<Portfolio> findByDesignerEmail_Email(String email);
}
