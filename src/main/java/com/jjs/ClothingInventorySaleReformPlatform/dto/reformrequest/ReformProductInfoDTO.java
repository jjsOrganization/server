package com.jjs.ClothingInventorySaleReformPlatform.dto.reformrequest;

import com.jjs.ClothingInventorySaleReformPlatform.domain.product.Product;
import com.jjs.ClothingInventorySaleReformPlatform.domain.product.ProductImg;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ReformProductInfoDTO {

    // 상품 상세 조회 용도
    private Long id;
    private String productName;  // 상품명
    private int price;  // 가격
    private List<ProductImg> productImg;

}
