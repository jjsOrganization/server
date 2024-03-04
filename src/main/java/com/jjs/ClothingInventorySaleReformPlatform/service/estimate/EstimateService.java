package com.jjs.ClothingInventorySaleReformPlatform.service.estimate;


import com.jjs.ClothingInventorySaleReformPlatform.controller.product.AuthenticationFacade;
import com.jjs.ClothingInventorySaleReformPlatform.domain.reformrequest.ReformRequest;
import com.jjs.ClothingInventorySaleReformPlatform.domain.user.DesignerInfo;
import com.jjs.ClothingInventorySaleReformPlatform.dto.reformrequest.ReformRequestCheckDTO;
import com.jjs.ClothingInventorySaleReformPlatform.repository.estimate.EstimateRepository;
import com.jjs.ClothingInventorySaleReformPlatform.repository.reformrequest.ReformRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.jjs.ClothingInventorySaleReformPlatform.dto.reformrequest.ReformRequestCheckDTO.convertToDTO;
@Service
@RequiredArgsConstructor
public class EstimateService {
    private final AuthenticationFacade authenticationFacade;

    private final EstimateRepository estimateRepository;
    private final ReformRequestRepository requestRepository;

    public List<ReformRequestCheckDTO> getRequestList(){
        DesignerInfo designerInfo = new DesignerInfo();
        designerInfo.setEmail(authenticationFacade.getCurrentUsername());

        List<ReformRequest> reformRequestsByDesignerEmail = requestRepository.findReformRequestsByDesignerEmail(designerInfo) // 의뢰서 조회
                .orElseThrow(() -> new IllegalArgumentException("요청받은 의뢰가 없습니다."));

        List<ReformRequestCheckDTO> reformRequestCheckDTOList = reformRequestsByDesignerEmail.stream() // 의뢰서 엔티티 -> DTO로 변경 후 반환
                .map(ReformRequestCheckDTO::convertToDTO)
                .collect(Collectors.toList());

        return reformRequestCheckDTOList;
        }
    }

