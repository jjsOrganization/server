package com.jjs.ClothingInventorySaleReformPlatform.domain.portfolio.dto.response;

import com.jjs.ClothingInventorySaleReformPlatform.domain.portfolio.entity.ReformOutput;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReformOutputListDTO {

    private String title; // 제목
    private String completeImgUrl;  // 리폼 완료 사진 -> 형상관리에서 completeImgUrl
    private Long progressNumber;

    public static ReformOutputListDTO convertToDTO(ReformOutput reformOutput) {
        ReformOutputListDTO reformOutputListDTO = new ReformOutputListDTO();
        reformOutputListDTO.setCompleteImgUrl(reformOutput.getCompleteImgUrl());
        reformOutputListDTO.setTitle(reformOutput.getTitle());
        reformOutputListDTO.setProgressNumber(reformOutput.getProgress().getId());

        return  reformOutputListDTO;
    }
}
