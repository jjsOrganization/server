package com.jjs.ClothingInventorySaleReformPlatform.domain.product.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jjs.ClothingInventorySaleReformPlatform.global.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "productImage")
public class ProductImg extends BaseTimeEntity {

    @Id
    @Column(name = "product_img_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "IMG_NAME", length = 1000)
    private String imgName; //이미지 파일명

    @Column(name = "ORI_IMG_NAME", length = 1000)
    private String oriImgName; //원본 이미지 파일명

    @Column(name = "IMG_URL", length = 1000)
    private String imgUrl; //이미지 조회 경로

    @Column(name = "REPIMG_YN", length = 1000)
    private String repimgYn; //대표 이미지 여부

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    public void updateItemImg(String oriImgName, String imgName, String imgUrl) {
        this.oriImgName = oriImgName;
        this.imgName = imgName;
        this.imgUrl = imgUrl;
    }
}
