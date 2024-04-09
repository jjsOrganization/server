package com.jjs.ClothingInventorySaleReformPlatform.dto.reform.estimate.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class EstimateRequestDTO {

    private String estimateInfo;  // 견적서 정보(내용)

    private List<MultipartFile> estimateImg;  // 견적서 사진

    private int reformPrice; // 희망 가격

}
