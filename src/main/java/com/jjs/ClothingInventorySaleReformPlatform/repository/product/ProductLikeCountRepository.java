package com.jjs.ClothingInventorySaleReformPlatform.repository.product;

import com.jjs.ClothingInventorySaleReformPlatform.domain.product.like.ProductLikeCount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductLikeCountRepository extends JpaRepository<ProductLikeCount, Long> {
    Optional<ProductLikeCount> findByProductId(Long productId);  // 상품 id로 좋아요 집계 정보 조회
}
