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

    // 삭제된 상품은 조회에 포함되지 않음
    List<Product> findByCreateByAndIsDeletedFalse(String createBy);  // createBy로 필터링하고, isDeleted가 false인 상품들만 조회
    List<Product> findByIsDeletedFalse();
    Optional<Product> findByIdAndIsDeletedFalse(Long id);
    List<Product> findByCategoryIdAndIsDeletedFalse(Long categoryId);
}
