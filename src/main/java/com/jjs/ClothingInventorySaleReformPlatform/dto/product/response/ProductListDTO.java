package com.jjs.ClothingInventorySaleReformPlatform.dto.product.response;

import com.jjs.ClothingInventorySaleReformPlatform.domain.product.ProductImg;
import com.jjs.ClothingInventorySaleReformPlatform.domain.product.ProductSellStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ProductListDTO {

    // 상품 전체 리스트로 조회 용도
    private Long id;
    private String productName;  // 상품명
    private int price;  // 가격
    private String itemDetail;
    private int productStock;  // 재고수
    private ProductSellStatus productSellStatus; // 상품 상태 (품절 or 판매 가능)[SELL, SOLD_OUT]

}
