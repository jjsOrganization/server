package com.jjs.ClothingInventorySaleReformPlatform.domain.product.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "CATEGORY")
public class Category {  // 카테고리

    @Id
    @Column(name="category_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "category_name", nullable = false)
    private String categoryName;

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    private List<Product> products = new ArrayList<>();


    @Column(name = "completed_product_count", nullable = false)
    private Long completedProductCount = 0L;  // 초기값을 0으로 설정

    public Category(String name) {
        this.categoryName = name;
    }

    /**
     * 상품 주문 완료 시, 상품에 해당되는 카테고리 수 증가
     * @param count
     */
    public void incrementCompletedProductCount(Long count) {
        this.completedProductCount += count;
    }

}