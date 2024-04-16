package com.jjs.ClothingInventorySaleReformPlatform.domain.reform.dto.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class ProgressImgRequestDTO {

    private Long estimateId;
    private MultipartFile imgUrl;
}
