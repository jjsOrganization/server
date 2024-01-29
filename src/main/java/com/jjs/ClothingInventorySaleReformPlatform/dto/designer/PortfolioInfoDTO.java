package com.jjs.ClothingInventorySaleReformPlatform.dto.designer;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * 포트폴리오 정보를 가져와서 저장하는 DTO
 * Optional 객체로 받아와서 반환하려 했으나, Optional -> Json 변환은 권장하지 않기에 DTO 새로 생성
 * 기존 포트폴리오 정보 저장하는 DTO에서는 이미지의 형태가 MultiPartFile이라서 저장되어 있는 이미지 경로를 받아오기 위해 DTO 새로 생성함
 */
@Getter
@Setter
public class PortfolioInfoDTO {

    @Schema(description = "자기소개 및 설명")
    private String explanation; // 자기소개 & 설명

    @Schema(description = "디자이너 이미지 경로")
    private String designerImagePath; // 디자이너 이미지 경로


}
