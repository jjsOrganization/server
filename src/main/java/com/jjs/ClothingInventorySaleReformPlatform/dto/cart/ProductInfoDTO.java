package com.jjs.ClothingInventorySaleReformPlatform.dto.cart;

import com.jjs.ClothingInventorySaleReformPlatform.domain.cart.CartProduct;
import com.jjs.ClothingInventorySaleReformPlatform.domain.product.Product;
import com.jjs.ClothingInventorySaleReformPlatform.domain.product.ProductSellStatus;
import com.jjs.ClothingInventorySaleReformPlatform.dto.product.ProductImgDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class ProductInfoDTO {

    private Long id;  // 상품 코드 (PK)
    private String productName;  // 상품명
    private int price;  // 가격
    private int count;  // 재고수
    //private String productDetailText;
    private ProductSellStatus productSellStatus; // 상품 상태 (품절 or 판매 가능)[SELL, SOLD_OUT]
    private List<ProductImgDTO> productImgs;

    // Product 엔티티를 받아서 DTO를 생성하는 정적 메서드
    public static ProductInfoDTO from(CartProduct cartProduct) {
        Product product = cartProduct.getProduct();
        ProductInfoDTO dto = new ProductInfoDTO();
        dto.setId(product.getId());
        dto.setProductName(product.getProductName());
        dto.setPrice(product.getPrice());
        dto.setCount(cartProduct.getCount()); // 상품 개수 설정
        dto.setProductSellStatus(ProductSellStatus.valueOf(product.getProductSellStatus().name()));

        // 상품 이미지 처리
        List<ProductImgDTO> imgDTOs = product.getProductImg().stream()
                .map(img -> new ProductImgDTO(img.getId(), img.getImgName(), img.getOriImgName(), img.getImgUrl(), img.getRepimgYn()))
                .collect(Collectors.toList());
        dto.setProductImgs(imgDTOs);

        return dto;
    }
}
