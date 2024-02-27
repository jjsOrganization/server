package com.jjs.ClothingInventorySaleReformPlatform.repository.product;

import com.jjs.ClothingInventorySaleReformPlatform.domain.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCreateBy(String createBy);
    List<Product> findByProductNameContaining(String keyword);

    Product findProductById (long id);
    List<Product> findByCategoryId(Long categoryId);  // 카테고리 ID를 기반으로 상품을 조회
}
