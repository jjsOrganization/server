package com.jjs.ClothingInventorySaleReformPlatform.domain.reform.estimate;

import com.jjs.ClothingInventorySaleReformPlatform.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "ESTIMATE_IMAGE")
public class EstimateImage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ESTIMATE_IMG_NUMBER", nullable = false)
    private Long id;  // 견적서 이미지 번호

    @Column(name = "ESTIMATE_IMG", nullable = false)
    private String imgUrl;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ESTIMATE_NUMBER")
    private Estimate estimate;
}
