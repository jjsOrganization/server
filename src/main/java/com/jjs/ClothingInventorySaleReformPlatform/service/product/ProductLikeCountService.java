package com.jjs.ClothingInventorySaleReformPlatform.service.product;

import com.jjs.ClothingInventorySaleReformPlatform.domain.product.Product;
import com.jjs.ClothingInventorySaleReformPlatform.domain.product.like.ProductLikeCount;
import com.jjs.ClothingInventorySaleReformPlatform.repository.product.ProductLikeCountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductLikeCountService {

    private final ProductLikeCountRepository productLikeCountRepository;

    @Transactional
    public void incrementLikeCount(Long productId) {
        ProductLikeCount likeCount = productLikeCountRepository.findByProductId(productId)
                .orElseGet(() -> new ProductLikeCount(null, new Product(productId), 0L));
        likeCount.setLikeCount(likeCount.getLikeCount() + 1);
        productLikeCountRepository.save(likeCount);
    }

    @Transactional
    public void decrementLikeCount(Long productId) {
        ProductLikeCount likeCount = productLikeCountRepository.findByProductId(productId)
                .orElseThrow(() -> new IllegalArgumentException("좋아요 count 정보를 찾을 수 없습니다."));
        if (likeCount.getLikeCount() > 0) {
            likeCount.setLikeCount(likeCount.getLikeCount() - 1);
            productLikeCountRepository.save(likeCount);
        } else {
            // 좋아요 수가 0 이하인 경우에 대한 예외 처리 또는 로그 기록
            log.info("좋아요 개수가 0개 이하입니다.");
        }
    }

    public Long getLikeCount(Long productId) {
        return productLikeCountRepository.findByProductId(productId)
                .map(ProductLikeCount::getLikeCount)
                .orElse(0L);
    }
}
