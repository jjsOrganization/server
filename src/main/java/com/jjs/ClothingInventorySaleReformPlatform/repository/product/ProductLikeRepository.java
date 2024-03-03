package com.jjs.ClothingInventorySaleReformPlatform.repository.product;

import com.jjs.ClothingInventorySaleReformPlatform.domain.product.Product;
import com.jjs.ClothingInventorySaleReformPlatform.domain.product.like.ProductLike;
import com.jjs.ClothingInventorySaleReformPlatform.domain.user.PurchaserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductLikeRepository extends JpaRepository<ProductLike, Long> {
    boolean existsByPurchaserAndProduct(PurchaserInfo purchaserInfo, Product product);
    void deleteByPurchaserAndProduct(PurchaserInfo purchaserInfo, Product product);
}
