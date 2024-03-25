package com.jjs.ClothingInventorySaleReformPlatform.dto.reformrequest;

import com.jjs.ClothingInventorySaleReformPlatform.domain.reformrequest.ReformRequest;
import com.jjs.ClothingInventorySaleReformPlatform.domain.reformrequest.ReformRequestStatus;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;


/**
 * 리폼 내역 리스트 불러올 때 사용하는 DTO
 */
@Getter
@Setter
public class ReformRequestResponseDTO {

    @NotEmpty
    private String requestPart; // 리폼 의뢰 부위

    @NotEmpty
    private String requestInfo;  // 의뢰 정보(내용)

    private List<ReformRequestImgDTO> requestImg;  // 의뢰 사진

    private String requestPrice; // 희망 가격

    private String designerEmail; // 요청할 디자이너 이메일

    private ReformRequestStatus requestStatus; // 의뢰서 요청 상태( 대기, 수락, 거절)

    /**
     * 조회된 의뢰 요청들을 DTO로 변경하는 메소드
     * @param reformRequests // 이메일을 통해 조회된 의뢰 목록
     * reformRequestCheckDTO = 의뢰서 DTO
     * reformRequestImgDTOList = 의뢰서에 포함된 이미지 리스트 엔티티 -> DTO로 변환한 변수
     */
    public static ReformRequestResponseDTO convertToDTO(ReformRequest reformRequests) {

            ReformRequestResponseDTO reformRequestResponseDTO = new ReformRequestResponseDTO();

            reformRequestResponseDTO.setDesignerEmail(reformRequests.getDesignerEmail().getEmail());
            reformRequestResponseDTO.setRequestInfo(reformRequests.getRequestInfo());
            reformRequestResponseDTO.setRequestPart(reformRequests.getRequestPart());
            reformRequestResponseDTO.setRequestPrice(reformRequests.getRequestPrice());
            reformRequestResponseDTO.setRequestStatus(reformRequests.getRequestStatus());

        List<ReformRequestImgDTO> reformRequestImgDTOList = reformRequests.getReformRequestImageList().stream() // 의뢰서에 포함된 이미지 엔티티 리스트 -> DTO 리스트 형태로 변경한 변수
                .map(ReformRequestImgDTO::toReformRequestImgDTO) // toReformRequestImgDTO -> 이미지 엔티티 리스트를 DTO 리스트로 변경
                .collect(Collectors.toList());

            reformRequestResponseDTO.setRequestImg(reformRequestImgDTOList);

            return reformRequestResponseDTO;
    }
}