package com.jjs.ClothingInventorySaleReformPlatform.dto.reformrequest;

import com.jjs.ClothingInventorySaleReformPlatform.domain.reform.reformrequest.ReformRequestImage;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReformRequestImgDTO {
    private Long id;  // 리폼 의뢰 이미지 번호

    private String imgUrl;


    // DTO 변환 메소드 추가
    public static ReformRequestImgDTO toReformRequestImgDTO(ReformRequestImage requestImage) {
        ReformRequestImgDTO reformRequestImgDTO = new ReformRequestImgDTO();
        reformRequestImgDTO.id = requestImage.getId();
        reformRequestImgDTO.imgUrl = requestImage.getImgUrl();
        return reformRequestImgDTO;
    }
}
