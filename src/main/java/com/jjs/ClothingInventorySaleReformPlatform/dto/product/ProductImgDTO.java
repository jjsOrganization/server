package com.jjs.ClothingInventorySaleReformPlatform.dto.product;

import com.jjs.ClothingInventorySaleReformPlatform.domain.product.ProductImg;
import lombok.*;
import org.modelmapper.ModelMapper;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductImgDTO {

    private Long id;

    private String imgName;
    private String oriImgName;
    private String imgUrl;
    private String repImgYn;

    // entity -> dto
    /*
    public static ProductImgDTO entityToDto(ProductImg productImg) {
        ProductImgDTO productImgDTO = ProductImgDTO.builder()
                .id(productImg.getId())
                .imgName(productImg.getImgName())
                .oriImgName(productImg.getOriImgName())
                .imgUrl(productImg.getImgUrl())
                .repImgYn(productImg.getRepimgYn())
                .build();
        return productImgDTO;
    }*/

    // ModelMapper가 위 entity -> dto 과정을 대체
    private static ModelMapper modelMapper = new ModelMapper();

    public static ProductImgDTO entityToDto(ProductImg productImg) {
        ProductImgDTO productImgDTO = modelMapper.map(productImg, ProductImgDTO.class);
        return productImgDTO;

    }
}
