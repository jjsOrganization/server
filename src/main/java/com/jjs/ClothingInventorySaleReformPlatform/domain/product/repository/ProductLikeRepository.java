package com.jjs.ClothingInventorySaleReformPlatform.domain.product.repository;

import com.jjs.ClothingInventorySaleReformPlatform.domain.product.entity.Product;
import com.jjs.ClothingInventorySaleReformPlatform.domain.product.entity.ProductLike;
import com.jjs.ClothingInventorySaleReformPlatform.domain.user.entity.PurchaserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductLikeRepository extends JpaRepository<ProductLike, Long> {
    boolean existsByPurchaserAndProduct(PurchaserInfo purchaserInfo, Product product);
    void deleteByPurchaserAndProduct(PurchaserInfo purchaserInfo, Product product);
}
