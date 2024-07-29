package com.jjs.ClothingInventorySaleReformPlatform.domain.product.dto.request;

import com.jjs.ClothingInventorySaleReformPlatform.domain.product.dto.ProductImgDTO;
import com.jjs.ClothingInventorySaleReformPlatform.domain.product.entity.Category;
import com.jjs.ClothingInventorySaleReformPlatform.domain.product.entity.Product;
import com.jjs.ClothingInventorySaleReformPlatform.domain.product.entity.ProductSellStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class ProductFormDTO {  // 화면단에서 정보를 가져올 것

    private Long id;  // 상품 코드 (PK)

    @NotBlank(message = "상품명은 필수 입력 값입니다.")
    private String productName;  // 상품명

    @NotNull(message = "가격은 필수 입력 값입니다.")
    private int price;  // 가격

    @NotBlank(message = "상품 설명은 필수 입력 값입니다.")
    private String itemDetail;

    @NotNull(message = "재고는 필수 입력 값입니다.")
    private int productStock;  // 재고수

    private ProductSellStatus productSellStatus; // 상품 상태 (품절 or 판매 가능)[SELL, SOLD_OUT]

    private Category categoryId;

    private List<ProductImgDTO> productImgDtoList = new ArrayList<>();  // 상품 이미지 리스트
    private List<Long> productImgIds = new ArrayList<>();  // 상품 아이디만 관리하는 리스트

    public Product toEntity() {
        return new Product(
                this.productName,
                this.price,
                this.productStock,
                this.itemDetail,
                this.productSellStatus = ProductSellStatus.SELL,
                this.categoryId
        );
    }
}
