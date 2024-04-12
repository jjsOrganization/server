package com.jjs.ClothingInventorySaleReformPlatform.domain.reform.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class ReformRequestDTO {

    @NotEmpty
    private String requestPart; // 리폼 의뢰 부위

    @NotEmpty
    private String requestInfo;  // 의뢰 정보(내용)

    private List<MultipartFile> requestImg;  // 의뢰 사진

    private String requestPrice; // 희망 가격

    private String designerEmail; // 요청할 디자이너 이메일


}
