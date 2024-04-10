package com.jjs.ClothingInventorySaleReformPlatform.dto.reform.estimate.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EstimateResponseDTO {

    private Long id;  // 견적서 번호
    private String clientEmail;  // 의뢰자 이메일
    private String designerEmail;  // 디자이너 이메일
    private String estimateInfo;  // 의뢰 정보(내용)
    private int price; // 리폼 가격
    private int totalPrice;  // 리폼 + 상품 가격
    private String estimateImg;  // 의뢰 사진
    private Long requestNumber;  // 의뢰서 번호


    private String estimateStatus;  // 견적서 상태 REQUEST_WAITING, REQUEST_REJECTED, REQUEST_ACCEPTED
}
