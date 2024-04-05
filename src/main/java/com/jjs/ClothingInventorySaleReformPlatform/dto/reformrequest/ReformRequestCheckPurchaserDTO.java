package com.jjs.ClothingInventorySaleReformPlatform.dto.reformrequest;

import com.jjs.ClothingInventorySaleReformPlatform.domain.portfolio.Portfolio;
import com.jjs.ClothingInventorySaleReformPlatform.domain.reform.reformrequest.ReformRequest;
import com.jjs.ClothingInventorySaleReformPlatform.domain.reform.reformrequest.ReformRequestStatus;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class ReformRequestCheckPurchaserDTO {

    @NotEmpty
    private String requestPart; // 리폼 의뢰 부위

    @NotEmpty
    private String requestInfo;  // 의뢰 정보(내용)

    private List<ReformRequestImgDTO> requestImg;  // 의뢰 사진

    private String requestPrice; // 희망 가격

    private String designerName; // 요청할 디자이너 이름 - 포트폴리오에서 가져와야 됨

    private ReformRequestStatus requestStatus; // 의뢰서 요청 상태( 대기, 수락, 거절)

    /**
     * 조회된 의뢰 요청들을 DTO로 변경하는 메소드
     * @param reformRequests // 이메일을 통해 조회된 의뢰 목록
     * reformRequestCheckDTO = 의뢰서 DTO
     * reformRequestImgDTOList = 의뢰서에 포함된 이미지 리스트 엔티티 -> DTO로 변환한 변수
     */
    public static ReformRequestCheckPurchaserDTO convertToDTO(ReformRequest reformRequests, Portfolio portfolio) {

        ReformRequestCheckPurchaserDTO reformRequestCheckPurchaserDTO = new ReformRequestCheckPurchaserDTO();

        reformRequestCheckPurchaserDTO.setDesignerName(portfolio.getName());
        reformRequestCheckPurchaserDTO.setRequestInfo(reformRequests.getRequestInfo());
        reformRequestCheckPurchaserDTO.setRequestPart(reformRequests.getRequestPart());
        reformRequestCheckPurchaserDTO.setRequestPrice(reformRequests.getRequestPrice());
        reformRequestCheckPurchaserDTO.setRequestStatus(reformRequests.getRequestStatus());

        List<ReformRequestImgDTO> reformRequestImgDTOList = reformRequests.getReformRequestImageList().stream() // 의뢰서에 포함된 이미지 엔티티 리스트 -> DTO 리스트 형태로 변경한 변수
                .map(ReformRequestImgDTO::toReformRequestImgDTO) // toReformRequestImgDTO -> 이미지 엔티티 리스트를 DTO 리스트로 변경
                .collect(Collectors.toList());

        reformRequestCheckPurchaserDTO.setRequestImg(reformRequestImgDTOList);

        return reformRequestCheckPurchaserDTO;
    }
}
