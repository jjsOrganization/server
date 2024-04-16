package com.jjs.ClothingInventorySaleReformPlatform.domain.product.dto.response;

import com.jjs.ClothingInventorySaleReformPlatform.domain.product.entity.Product;
import com.jjs.ClothingInventorySaleReformPlatform.domain.product.entity.ProductImg;
import com.jjs.ClothingInventorySaleReformPlatform.domain.product.entity.ProductSellStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ProductDetailDTO {

    // 상품 상세 조회 용도
    private Long id;
    private String productName;  // 상품명
    private int price;  // 가격
    private String itemDetail;
    private int productStock;  // 재고수
    private ProductSellStatus productSellStatus; // 상품 상태 (품절 or 판매 가능)[SELL, SOLD_OUT]
    private List<ProductImg> productImg;

    public ProductDetailDTO(Product product) {
        this.id = product.getId();
        this.productName = product.getProductName();
        this.price = product.getPrice();
        this.itemDetail = product.getProductDetailText();
        this.productStock = product.getProductStock();
        this.productSellStatus = product.getProductSellStatus();
    }


}
