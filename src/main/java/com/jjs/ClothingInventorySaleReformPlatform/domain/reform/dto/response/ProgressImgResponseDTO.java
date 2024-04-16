package com.jjs.ClothingInventorySaleReformPlatform.domain.reform.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProgressImgResponseDTO {

    private Long progressId;
    private Long requestId;
    private Long estimateId;
    private String productImgUrl;  // 리폼 전 사진 - 상품 사진
    private String firstImgUrl;  // 리폼 중 사진 1
    private String secondImgUrl;  // 리폼 중 사진 2
    private String completeImgUrl;  // 리폼 완료 사진
}
