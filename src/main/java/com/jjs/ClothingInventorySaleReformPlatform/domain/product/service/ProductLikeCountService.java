package com.jjs.ClothingInventorySaleReformPlatform.domain.product.service;

import com.jjs.ClothingInventorySaleReformPlatform.domain.product.entity.Product;
import com.jjs.ClothingInventorySaleReformPlatform.domain.product.entity.ProductLikeCount;
import com.jjs.ClothingInventorySaleReformPlatform.domain.product.dto.response.ProductListLikeDTO;
import com.jjs.ClothingInventorySaleReformPlatform.domain.product.repository.ProductLikeCountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductLikeCountService {

    private final ProductLikeCountRepository productLikeCountRepository;

    /**
     * 상품에 대하여 구매자들이 좋아요를 하면 좋아요 개수 누적(증가)
     * @param productId
     */
    @Transactional
    public void incrementLikeCount(Long productId) {
        ProductLikeCount likeCount = productLikeCountRepository.findByProductId(productId)
                .orElseGet(() -> new ProductLikeCount(null, new Product(productId), 0L));
        likeCount.setLikeCount(likeCount.getLikeCount() + 1);
        productLikeCountRepository.save(likeCount);
    }

    /**
     * 상품에 대하여 구매자들이 좋아요를 취소하면 좋아요 개수 감소
     * @param productId
     */
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

    /**
     * 특정 상품에 대한 좋아요 개수 조회
     * @param productId
     * @return
     */
    public Long getLikeCount(Long productId) {
        return productLikeCountRepository.findByProductId(productId)
                .map(ProductLikeCount::getLikeCount)
                .orElse(0L);
    }

    /**
     * 상품들을 좋아요 개수 순서로 내림차순 정렬하여 목록으로 조회
     * @return
     */
    public List<ProductListLikeDTO> getProductsOrderByLikeCountDesc() {
        List<ProductLikeCount> likeCounts = productLikeCountRepository.findAllByOrderByLikeCountDesc();
        return likeCounts.stream()
                .map(likeCount -> productsFindAll(likeCount.getProduct(), likeCount.getLikeCount()))
                .collect(Collectors.toList());
    }



    private ProductListLikeDTO productsFindAll(Product product, Long likeCount) {  // 상품 전체 조회 dto
        ProductListLikeDTO dto = new ProductListLikeDTO();
        dto.setId(product.getId());
        dto.setProductName(product.getProductName());
        dto.setPrice(product.getPrice());
        dto.setItemDetail(product.getProductDetailText());
        dto.setProductStock(product.getProductStock());
        dto.setProductSellStatus(product.getProductSellStatus());
        dto.setLikeCount(likeCount);
        // 카테고리 이름 설정
        if (product.getCategory() != null) {
            dto.setCategoryName(product.getCategory().getCategoryName());
        }

        // 상품 이미지 추가함...
        if (!product.getProductImg().isEmpty()) {
            dto.setImgUrl(product.getProductImg().get(0).getImgUrl());
        } else {
            dto.setImgUrl(null);
        }
        return dto;
    }
}
