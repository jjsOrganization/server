package com.jjs.ClothingInventorySaleReformPlatform.repository.product;

import com.jjs.ClothingInventorySaleReformPlatform.domain.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
