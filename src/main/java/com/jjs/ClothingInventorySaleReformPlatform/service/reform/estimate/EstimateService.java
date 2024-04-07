package com.jjs.ClothingInventorySaleReformPlatform.service.reform.estimate;


import com.jjs.ClothingInventorySaleReformPlatform.controller.product.AuthenticationFacade;
import com.jjs.ClothingInventorySaleReformPlatform.domain.reform.estimate.Estimate;
import com.jjs.ClothingInventorySaleReformPlatform.domain.reform.estimate.EstimateImage;
import com.jjs.ClothingInventorySaleReformPlatform.domain.reform.estimate.EstimateStatus;
import com.jjs.ClothingInventorySaleReformPlatform.domain.reform.reformrequest.ReformRequest;
import com.jjs.ClothingInventorySaleReformPlatform.domain.reform.reformrequest.ReformRequestStatus;
import com.jjs.ClothingInventorySaleReformPlatform.domain.user.DesignerInfo;
import com.jjs.ClothingInventorySaleReformPlatform.dto.reform.estimate.request.EstimateRequestDTO;
import com.jjs.ClothingInventorySaleReformPlatform.dto.reform.estimate.response.EstimateResponseDTO;
import com.jjs.ClothingInventorySaleReformPlatform.dto.reform.reformrequest.ReformRequestResponseDTO;
import com.jjs.ClothingInventorySaleReformPlatform.repository.reform.progressmanagement.ProgressRepository;
import com.jjs.ClothingInventorySaleReformPlatform.repository.reform.estimate.EstimateImgRepository;
import com.jjs.ClothingInventorySaleReformPlatform.repository.reform.estimate.EstimateRepository;
import com.jjs.ClothingInventorySaleReformPlatform.repository.reform.reformrequest.ReformRequestRepository;
import com.jjs.ClothingInventorySaleReformPlatform.service.s3.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class EstimateService {
    private final AuthenticationFacade authenticationFacade;
    private final S3Service s3Service;

    private final EstimateRepository estimateRepository;
    private final EstimateImgRepository estimateImgRepository;
    private final ReformRequestRepository requestRepository;
    private final ProgressRepository progressRepository;

    /**
     * 로그인한 디자이너의 요청받은 모든 의뢰 리스트를 불러오는 메소드
     * @return
     */
    @Transactional
    public List<ReformRequestResponseDTO> getAllRequestList(){
        DesignerInfo designerInfo = new DesignerInfo();
        designerInfo.setEmail(authenticationFacade.getCurrentUsername());

        List<ReformRequest> reformRequestsByDesignerEmail = requestRepository.findReformRequestsByDesignerEmail(designerInfo) // 의뢰서 조회
                .orElseThrow(() -> new IllegalArgumentException("요청받은 의뢰가 없습니다."));

        List<ReformRequestResponseDTO> reformRequestResponseDTOList = reformRequestsByDesignerEmail.stream() // 의뢰서 엔티티 -> DTO로 변경 후 반환
                .map(ReformRequestResponseDTO::convertToDTO) // 2차원 배열 형태로 의뢰서 데이터가 들어옴. 각 배열에 대해 convert 적용
                .collect(Collectors.toList());

        return reformRequestResponseDTOList;
        }

    /**
     * 의뢰 상세 조회 하는 메소드
     * @param requestNumber
     * @return
     */
    @Transactional
    public List<ReformRequestResponseDTO> getRequestListByNumber(Long requestNumber) {
        Optional<ReformRequest> reformRequestById = requestRepository.findReformRequestById(requestNumber);

        List<ReformRequestResponseDTO> reformRequestResponseDTOList = reformRequestById
                .stream()
                .map(ReformRequestResponseDTO::convertToDTO)
                .collect(Collectors.toList());

        return reformRequestResponseDTOList;
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

    /**
     * 견적서 작성 후 저장
     * @param estimateRequestDTO
     * @param requestNumber
     */
    @Transactional
    public void saveEstimate(EstimateRequestDTO estimateRequestDTO, Long requestNumber) {

        ReformRequest reformRequest = requestRepository.findAllById(requestNumber);

        Estimate estimate = new Estimate();
        estimate.setPurchaserEmail(reformRequest.getPurchaserEmail());
        estimate.setDesignerEmail(reformRequest.getDesignerEmail());
        estimate.setRequestNumber(reformRequest);
        estimate.setEstimateStatus(EstimateStatus.REQUEST_WAITING);

        estimate.setEstimateInfo(estimateRequestDTO.getEstimateInfo());
        estimate.setPrice(estimateRequestDTO.getPrice());
        estimateRepository.save(estimate);

        saveImageList(estimateRequestDTO, estimate);
    }

    /**
     * 견적서 작성 및 수정 시 사진이 저장되는 메서드
     * @param estimateRequestDTO
     * @param estimate
     */
    @Transactional
    public void saveImageList(EstimateRequestDTO estimateRequestDTO, Estimate estimate) {
        estimateRequestDTO.getEstimateImg().forEach(image -> {
            try {
                EstimateImage estimateImage = new EstimateImage();
                estimateImage.setImgUrl(s3Service.uploadFile(image, "EstimateImage/"));
                estimateImage.setEstimate(estimate);
                estimateImgRepository.save(estimateImage);
            } catch (IOException e) {
                log.error(e.getMessage());
                throw new RuntimeException("이미지가 없습니다.");
            }
        });
    }

    /**
     * 견적서 조회
     * @param estimateNumber
     * @return
     */
    @Transactional
    public EstimateResponseDTO getEstimate(Long estimateNumber) {
        Estimate estimateById = estimateRepository.findEstimateById(estimateNumber)
                .orElseThrow(() -> new IllegalStateException("견적서가 존재하지 않습니다."));
        EstimateResponseDTO estimateResponseDTO = new EstimateResponseDTO();
        estimateResponseDTO.setId(estimateById.getId());
        estimateResponseDTO.setClientEmail(estimateById.getPurchaserEmail().getEmail());
        estimateResponseDTO.setDesignerEmail(estimateById.getDesignerEmail().getEmail());
        estimateResponseDTO.setEstimateInfo(estimateById.getEstimateInfo());
        estimateResponseDTO.setPrice(estimateById.getPrice());
        estimateResponseDTO.setEstimateImg(estimateById.getEstimateImg().get(0).getImgUrl());
        estimateResponseDTO.setRequestNumber(estimateById.getRequestNumber().getId());
        estimateResponseDTO.setEstimateStatus(estimateById.getEstimateStatus().name());

        return estimateResponseDTO;
    }

    /**
     * 견적서 수정
     * @param estimateRequestDTO
     * @param estimateNumber
     * @throws IOException
     */
    @Transactional
    public void updateEstimate(EstimateRequestDTO estimateRequestDTO, Long estimateNumber) throws IOException {

        Estimate estimate = estimateRepository.findEstimateById(estimateNumber)
                .orElseThrow(() -> new IllegalArgumentException("견적서가 존재하지 않습니다."));

        if (estimate.getEstimateStatus() != EstimateStatus.REQUEST_WAITING) {
            throw new RuntimeException("이미 진행 중인 의뢰로 수정이 불가합니다.");
        } else {
            List<EstimateImage> imageList = estimateImgRepository.findAllByEstimateId(estimateNumber);
            estimate.setEstimateInfo(estimateRequestDTO.getEstimateInfo());
            estimate.setPrice(estimateRequestDTO.getPrice());
            estimateRepository.save(estimate);

            for (EstimateImage estimateImage : imageList) {
                if (!estimateImage.getImgUrl().isEmpty() && estimateImage.getImgUrl() != null) {
                    s3Service.fileDelete(estimateImage.getImgUrl());
                    estimateImgRepository.deleteByEstimateId(estimateImage.getEstimate().getId());
                }
            }
            saveImageList(estimateRequestDTO, estimate);

        }
    }

    @Transactional
    public void selEstimateStatus(Long estimateNumber, EstimateStatus status) {
        Estimate estimate = estimateRepository.findEstimateById(estimateNumber)
                .orElseThrow(() -> new IllegalArgumentException("견적서가 존재하지 않습니다."));

        if (estimate.getEstimateStatus() != EstimateStatus.REQUEST_WAITING) {
            throw new RuntimeException("이미 진행 중인 의뢰로 수정이 불가합니다.");
        } else {
            estimate.setEstimateStatus(status);
            estimateRepository.save(estimate);
        }
    }


}

