package com.jjs.ClothingInventorySaleReformPlatform.domain.product;

import com.jjs.ClothingInventorySaleReformPlatform.common.entity.BaseEntity;
import com.jjs.ClothingInventorySaleReformPlatform.dto.product.ProductFormDTO;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;


@Getter
@Setter
@Entity
@Table(name = "PRODUCT")  // goods
@NoArgsConstructor
public class Product extends BaseEntity {  // BaseEntity가 등록시간, 수정시간, 만든 사람, 수정자 정보 포함해줌
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // 기본키 자동 생성 - IDENTITY : 기본키 생성을 db에 위임(원래 회원도 이걸로 권장)
    @Column(name = "product_id")
    private Long id;  // 상품 코드 (PK)

    @Column(name = "product_name", nullable = false, length = 30)
    private String productName;  // 상품명

    @Column(nullable = false)
    private int price;  // 가격

    @Column(nullable = false)
    private int productStock;  // 재고수

    @Column
    private String productDetailText;  // 상품 설명(보류)

    @Enumerated(EnumType.STRING)
    private ProductSellStatus productSellStatus; // 상품 상태 (품절 or 판매 가능)[SELL, SOLD_OUT]

    // 상품 삭제 시 이미지 DB 도 같이 삭제 , cascade 옵션
    @OneToMany(mappedBy = "product", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @OrderBy("id asc")
    private List<ProductImg> productImg;

    public void updateProduct(ProductFormDTO productFormDTO) {
        this.productName = productFormDTO.getProductName();
        this.price = productFormDTO.getPrice();
        this.productStock = productFormDTO.getProductStock();
        this.productDetailText = productFormDTO.getItemDetail();
        this.productSellStatus = productFormDTO.getProductSellStatus();
    }


/*
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "categoryId")
    private Category category;  // 카테고리 (상품 검색의 카테고리 선택을 위해 클래스 파일로 분류)
    */
    /*
    @OneToOne
    @JoinColumn(name = "colorId")
    private ColorEntity color;  // 상품 컬러 (보류)
     */

}