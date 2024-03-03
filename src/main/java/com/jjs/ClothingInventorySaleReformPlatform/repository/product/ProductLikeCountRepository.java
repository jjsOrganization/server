package com.jjs.ClothingInventorySaleReformPlatform.repository.product;

import com.jjs.ClothingInventorySaleReformPlatform.domain.product.like.ProductLikeCount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductLikeCountRepository extends JpaRepository<ProductLikeCount, Long> {
    Optional<ProductLikeCount> findByProductId(Long productId);  // 상품 id로 좋아요 집계 정보 조회
    List<ProductLikeCount> findAllByOrderByLikeCountDesc();  // ProductLikeCount 엔티티를 좋아요 개수의 내림차순으로 정렬하여 가져옴
}
