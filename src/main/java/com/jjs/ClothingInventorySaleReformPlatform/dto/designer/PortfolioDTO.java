package com.jjs.ClothingInventorySaleReformPlatform.dto.designer;

import com.jjs.ClothingInventorySaleReformPlatform.domain.Portfolio;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class PortfolioDTO {

    private String explanation; // 자기소개 & 설명

    private String preResultsExplanation; // 결과물 설명

    private MultipartFile preResultsImage; // 결과물 이미지

    private String designerEmail; // 디자이너 이메일



}
