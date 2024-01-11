package com.jjs.ClothingInventorySaleReformPlatform.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "CATEGORY", schema = "jjs")
public class Category {  // 카테고리
    @Id
    @Size(max = 30)
    @Column(name = "CATEGORY_NAME", nullable = false, length = 30)
    private String categoryName;  // 카테고리명

    @NotNull
    @Column(name = "WATER_USAGE_CATEGORY", nullable = false)
    private Integer waterUsageCategory;  // 카테고리별 물 사용량

    @OneToMany(mappedBy = "categoryName")
    private Set<Watersaved> watersaveds = new LinkedHashSet<>();

}