package com.jjs.ClothingInventorySaleReformPlatform.dto.product;

import com.jjs.ClothingInventorySaleReformPlatform.domain.SellerInfo;
import com.jjs.ClothingInventorySaleReformPlatform.domain.product.Category;
import com.jjs.ClothingInventorySaleReformPlatform.domain.product.Product;
import com.jjs.ClothingInventorySaleReformPlatform.domain.product.ProductSellStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
//import org.modelmapper.ModelMapper;

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



    private List<ProductImgDTO> productImgDtoList = new ArrayList<>();  // 상품 이미지 리스트
    private List<Long> productImgIds = new ArrayList<>();  // 상품 아이디만 관리하는 리스트



/*
    private static ModelMapper modelMapper = new ModelMapper();

    public Product createProduct() {
        Product product = modelMapper.map(this, Product.class);
        return product;
    }


    public static ProductFormDTO entityToDto(Product product) {
        ProductFormDTO productFormDTO = modelMapper.map(product, ProductFormDTO.class);
        return productFormDTO;
    }

 */

    /*
    private Category category;  // 카테고리 (상품 검색의 카테고리 선택을 위해 클래스 파일로 분류)
     */
}
