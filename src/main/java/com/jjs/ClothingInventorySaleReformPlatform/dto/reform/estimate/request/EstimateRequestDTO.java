package com.jjs.ClothingInventorySaleReformPlatform.dto.reform.estimate.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class EstimateRequestDTO {

    private String estimateInfo;  // 의뢰 정보(내용)

    private List<MultipartFile> estimateImg;  // 의뢰 사진

    private String price; // 희망 가격

}
