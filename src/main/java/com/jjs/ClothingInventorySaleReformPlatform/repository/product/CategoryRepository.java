package com.jjs.ClothingInventorySaleReformPlatform.repository.product;

import com.jjs.ClothingInventorySaleReformPlatform.domain.product.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
