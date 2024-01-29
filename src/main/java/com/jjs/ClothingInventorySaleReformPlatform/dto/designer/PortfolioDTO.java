package com.jjs.ClothingInventorySaleReformPlatform.dto.designer;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class PortfolioDTO {

    @NotBlank(message = "해당란은 필수 입력 값입니다.")
    @Schema(description = "자기소개 및 경력 설명")
    private String explanation; // 자기소개 & 설명

//    @NotBlank(message = "설명은 필수 입력 값입니다.")
//    private String preResultsExplanation; // 결과물 설명

    @NotNull(message = " 디자이너 이미지는 필수 입력 값입니다.")
    @Schema(description = "디자이너 이미지")
    private MultipartFile designerImage; // 디자이너 이미지


    @NotBlank(message = " 디자이너 이메일은 필수 입력 값입니다.")
    @Schema(description = "디자이너 이메일")
    private String designerEmail; // 디자이너 이메일



}
