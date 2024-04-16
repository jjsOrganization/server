package com.jjs.ClothingInventorySaleReformPlatform.domain.product.repository;

import com.jjs.ClothingInventorySaleReformPlatform.domain.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCreateBy(String createBy);
    List<Product> findByProductNameContaining(String keyword);

    Product findProductById (long id);
    List<Product> findByCategoryId(Long categoryId);  // 카테고리 ID를 기반으로 상품을 조회

    Optional<Product> findById(Long id);
}
