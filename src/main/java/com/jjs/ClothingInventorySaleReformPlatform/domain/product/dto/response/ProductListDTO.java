package com.jjs.ClothingInventorySaleReformPlatform.domain.product.dto.response;

import com.jjs.ClothingInventorySaleReformPlatform.domain.product.entity.ProductSellStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private String imgUrl;
    private String categoryName;
    private Long likeCount;  // 좋아요 개수
}
