package com.jjs.ClothingInventorySaleReformPlatform.domain.product.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SaveWaterDTO {

    private Long top;  // 상의
    private Long outer;  // 아우터
    private Long bottoms;  // 하의
    private Long skirt;  // 스커트
    private Long one_piece;  // 원피스
    private Long hat;  // 모자
    private Long socks;  // 양말 or 레그웨어
    private Long underwear;  // 속옷
    private Long accessories;  // 액세서리

}
