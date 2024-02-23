package com.jjs.ClothingInventorySaleReformPlatform.dto.reformrequest;

import com.jjs.ClothingInventorySaleReformPlatform.domain.product.Product;
import com.jjs.ClothingInventorySaleReformPlatform.domain.product.ProductImg;

import java.util.List;

public class ReformProductInfoDTO {

    // 상품 상세 조회 용도
    private Long id;
    private String productName;  // 상품명
    private int price;  // 가격
    private String productImg;

    public ReformProductInfoDTO(Product product) {
        this.id = product.getId();
        this.productName = product.getProductName();
        this.price = product.getPrice();
        this.productImg = product.getProductImg().get(0).getImgUrl();

    }
}
