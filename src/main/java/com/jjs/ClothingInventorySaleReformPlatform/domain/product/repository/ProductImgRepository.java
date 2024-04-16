package com.jjs.ClothingInventorySaleReformPlatform.domain.product.repository;

import com.jjs.ClothingInventorySaleReformPlatform.domain.product.entity.ProductImg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductImgRepository extends JpaRepository<ProductImg, Long> {
    List<ProductImg> findByProductId(Long productId);
}
