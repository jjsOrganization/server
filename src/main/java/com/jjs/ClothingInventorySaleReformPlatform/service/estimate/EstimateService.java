package com.jjs.ClothingInventorySaleReformPlatform.service.estimate;


import com.jjs.ClothingInventorySaleReformPlatform.controller.product.AuthenticationFacade;
import com.jjs.ClothingInventorySaleReformPlatform.domain.reformrequest.ReformRequest;
import com.jjs.ClothingInventorySaleReformPlatform.domain.reformrequest.ReformRequestStatus;
import com.jjs.ClothingInventorySaleReformPlatform.domain.user.DesignerInfo;
import com.jjs.ClothingInventorySaleReformPlatform.dto.reformrequest.ReformRequestCheckDTO;
import com.jjs.ClothingInventorySaleReformPlatform.repository.estimate.EstimateRepository;
import com.jjs.ClothingInventorySaleReformPlatform.repository.reformrequest.ReformRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EstimateService {
    private final AuthenticationFacade authenticationFacade;

    private final EstimateRepository estimateRepository;
    private final ReformRequestRepository requestRepository;

    /**
     * 로그인한 디자이너의 요청받은 모든 의뢰 리스트를 불러오는 메소드
     * @return
     */
    @Transactional
    public List<ReformRequestCheckDTO> getAllRequestList(){
        DesignerInfo designerInfo = new DesignerInfo();
        designerInfo.setEmail(authenticationFacade.getCurrentUsername());

        List<ReformRequest> reformRequestsByDesignerEmail = requestRepository.findReformRequestsByDesignerEmail(designerInfo) // 의뢰서 조회
                .orElseThrow(() -> new IllegalArgumentException("요청받은 의뢰가 없습니다."));

        List<ReformRequestCheckDTO> reformRequestCheckDTOList = reformRequestsByDesignerEmail.stream() // 의뢰서 엔티티 -> DTO로 변경 후 반환
                .map(ReformRequestCheckDTO::convertToDTO) // 2차원 배열 형태로 의뢰서 데이터가 들어옴. 각 배열에 대해 convert 적용
                .collect(Collectors.toList());

        return reformRequestCheckDTOList;
        }

    /**
     * 의뢰 상세 조회 하는 메소드
     * @param requestNumber
     * @return
     */
    @Transactional
    public List<ReformRequestCheckDTO> getRequestListByNumber(Long requestNumber) {
        Optional<ReformRequest> reformRequestById = requestRepository.findReformRequestById(requestNumber);

        List<ReformRequestCheckDTO> reformRequestCheckDTOList = reformRequestById
                .stream()
                .map(ReformRequestCheckDTO::convertToDTO)
                .collect(Collectors.toList());

        return reformRequestCheckDTOList;
    }



    /**
     * 요청받은 의뢰의 상태를 변경하는 메소드(의뢰 수락 or 의뢰 거절 상황)
     * @param requestNumber
     * @param clientResponse
     * @throws Exception
     */
    @Transactional
    public void updateRequestStatus(Long requestNumber, String clientResponse) throws Exception {

        ReformRequest reformRequestById = requestRepository.findReformRequestById(requestNumber)
                .orElseThrow(() -> new IllegalArgumentException("의뢰서가 존재하지 않습니다."));
        ReformRequestStatus requestStatus = reformRequestById.getRequestStatus();
        try{
            if(requestStatus.equals(ReformRequestStatus.REQUEST_WAITING)){
                if(clientResponse.equals("수락")){
                    reformRequestById.setId(requestNumber);
                    reformRequestById.setRequestStatus(ReformRequestStatus.REQUEST_ACCEPTED);

                } else if (clientResponse.equals("거절") ) {
                    reformRequestById.setId(requestNumber);
                    reformRequestById.setRequestStatus(ReformRequestStatus.REQUEST_REJECTED);

                }
            }else{
                throw new Exception("대기 상태가 아닙니다.");
            }
        }catch (Exception e){
            throw new Exception("상태 변경에 실패하였습니다.",e);
        }

    }
}

