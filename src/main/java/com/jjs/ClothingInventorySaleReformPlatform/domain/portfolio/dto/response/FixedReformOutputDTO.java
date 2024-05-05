package com.jjs.ClothingInventorySaleReformPlatform.domain.portfolio.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FixedReformOutputDTO {

    private String designerName;  // 디자이너 명

    private String productImgUrl;  // 리폼 전 사진 - 상품 사진 -> 형상관리의 productImgUrl
    private String reformRequestImgUrl;  // 구매자 요청 사진 -> 요청서에서 [0]사진만 가져옴
    private String estimateImgUrl;  // 디자이너 견적서 사진 -> 견적서에서 가져옴
    private String completeImgUrl;  // 리폼 완료 사진 -> 형상관리에서 completeImgUrl

    private String workingPeriod;  // 리폼 기간 -> 형상관리 엔티티 마지막 업데이트일 - 요청서 엔티티의 등록일
    private String category;  // 리폼 부위
    private int price;  // 리폼 가격
}
