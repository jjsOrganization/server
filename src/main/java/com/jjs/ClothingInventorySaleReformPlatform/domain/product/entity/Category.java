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


    @Column(name = "water_usage_category")
    private Integer waterUsageCategory;  // 카테고리별 물 사용량

    public Category(String name) {
        this.categoryName = name;
    }

}