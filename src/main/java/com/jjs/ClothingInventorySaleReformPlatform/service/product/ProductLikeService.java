package com.jjs.ClothingInventorySaleReformPlatform.service.product;

import com.jjs.ClothingInventorySaleReformPlatform.domain.product.Product;
import com.jjs.ClothingInventorySaleReformPlatform.domain.product.like.ProductLike;
import com.jjs.ClothingInventorySaleReformPlatform.domain.user.PurchaserInfo;
import com.jjs.ClothingInventorySaleReformPlatform.repository.auth.PurchaserRepository;
import com.jjs.ClothingInventorySaleReformPlatform.repository.product.ProductLikeRepository;
import com.jjs.ClothingInventorySaleReformPlatform.repository.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductLikeService {

    private final ProductLikeRepository productLikeRepository;
    private final PurchaserRepository purchaserRepository;
    private final ProductRepository productRepository;

    /**
     * 상품 상세에서 좋아요 누르면 좋하요 정보 추가
     * @param productId
     */
    public void addLike(Long productId) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        PurchaserInfo purchaser = purchaserRepository.findById(currentUsername)
                .orElseThrow(() -> new IllegalArgumentException("구매자를 찾을 수 없습니다. -> 로그인 확인 필요"));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));

        if (!productLikeRepository.existsByPurchaserAndProduct(purchaser, product)) {
            ProductLike productLike = new ProductLike();
            productLike.setPurchaser(purchaser);
            productLike.setProduct(product);
            productLikeRepository.save(productLike);
        }
    }

    /**
     * 상품 상세에서 꽉 찬 좋아요 누르면 좋아요 취소 -> 좋아요 한 정보 삭제
     * @param productId
     */
    public void removeLike(Long productId) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        PurchaserInfo purchaser = purchaserRepository.findById(currentUsername)
                .orElseThrow(() -> new IllegalArgumentException("구매자를 찾을 수 없습니다. -> 로그인 확인 필요"));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));

        productLikeRepository.deleteByPurchaserAndProduct(purchaser, product);
    }

    /**
     * 상품 상세 페이지를 로딩할 때 현재 로그인한 사용자가 해당 상품에 대해 좋아요를 눌렀는지의 여부를 반환
     * @param productId
     * @return
     */
    public boolean isLikedByPurchaser(Long productId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        PurchaserInfo purchaser = purchaserRepository.findById(currentUsername)
                .orElseThrow(() -> new IllegalArgumentException("구매자를 찾을 수 없습니다. -> 로그인 확인"));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));

        return productLikeRepository.existsByPurchaserAndProduct(purchaser, product);
    }
}
