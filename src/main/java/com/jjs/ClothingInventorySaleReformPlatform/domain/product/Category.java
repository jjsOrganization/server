package com.jjs.ClothingInventorySaleReformPlatform.domain.product;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "CATEGORY")
public class Category {  // 카테고리

    @Id
    @Column(name="categoryId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;

    // 카테고리명, 카테고리별 물 사용량
    @Column( name="categoryName", nullable=false, length=100 )
    private String categoryName;

    @Column(name = "WATER_USAGE_CATEGORY", nullable = false)
    private Integer waterUsageCategory;  // 카테고리별 물 사용량

    @Builder
    public Category(String categoryName) {
        this.categoryName = categoryName;
    }
/*
    @OneToMany(mappedBy = "categoryName")
    private Set<Watersaved> watersaveds = new LinkedHashSet<>();
*/
}