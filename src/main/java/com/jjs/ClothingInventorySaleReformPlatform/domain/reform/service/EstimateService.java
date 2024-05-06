package com.jjs.ClothingInventorySaleReformPlatform.domain.reform.service;


import com.jjs.ClothingInventorySaleReformPlatform.global.common.authentication.AuthenticationFacade;
import com.jjs.ClothingInventorySaleReformPlatform.domain.reform.entity.estimate.Estimate;
import com.jjs.ClothingInventorySaleReformPlatform.domain.reform.entity.estimate.EstimateImage;
import com.jjs.ClothingInventorySaleReformPlatform.domain.reform.entity.estimate.EstimateStatus;
import com.jjs.ClothingInventorySaleReformPlatform.domain.reform.entity.reformOrder.ReformOrder;
import com.jjs.ClothingInventorySaleReformPlatform.domain.reform.entity.reformOrder.ReformOrderStatus;
import com.jjs.ClothingInventorySaleReformPlatform.domain.reform.entity.progressManagement.ProgressStatus;
import com.jjs.ClothingInventorySaleReformPlatform.domain.reform.entity.progressManagement.Progressmanagement;
import com.jjs.ClothingInventorySaleReformPlatform.domain.reform.entity.reformRequest.ReformRequest;
import com.jjs.ClothingInventorySaleReformPlatform.domain.reform.entity.reformRequest.ReformRequestStatus;
import com.jjs.ClothingInventorySaleReformPlatform.domain.user.entity.DesignerInfo;
import com.jjs.ClothingInventorySaleReformPlatform.domain.reform.dto.request.EstimateRequestDTO;
import com.jjs.ClothingInventorySaleReformPlatform.domain.reform.dto.request.ReformOrderRequestDTO;
import com.jjs.ClothingInventorySaleReformPlatform.domain.reform.dto.response.EstimateResponseDTO;
import com.jjs.ClothingInventorySaleReformPlatform.domain.reform.dto.response.ReformOrderResponseDTO;
import com.jjs.ClothingInventorySaleReformPlatform.domain.reform.dto.response.ReformRequestResponseDTO;
import com.jjs.ClothingInventorySaleReformPlatform.domain.reform.repository.ReformOrderRepository;
import com.jjs.ClothingInventorySaleReformPlatform.domain.reform.repository.ProgressRepository;
import com.jjs.ClothingInventorySaleReformPlatform.domain.reform.repository.EstimateImgRepository;
import com.jjs.ClothingInventorySaleReformPlatform.domain.reform.repository.EstimateRepository;
import com.jjs.ClothingInventorySaleReformPlatform.domain.reform.repository.ReformRequestRepository;
import com.jjs.ClothingInventorySaleReformPlatform.global.s3.S3Service;
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
    private final ReformOrderRepository reformOrderRepository;
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
        estimate.setEstimateStatus(EstimateStatus.WRITING);

        estimate.setEstimateInfo(estimateRequestDTO.getEstimateInfo());
        estimate.setReformPrice(estimateRequestDTO.getReformPrice());  // 리폼 비용

        int totalPrice = (reformRequest.getProductNumber().getPrice() + estimateRequestDTO.getReformPrice());
        estimate.setPrice(totalPrice);
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

    public void submitEstimate(Long requestNumber) throws Exception {
        ReformRequest reformRequest = requestRepository.findReformRequestById(requestNumber)
                .orElseThrow(() -> new IllegalStateException("요청서가 존재하지 않습니다."));
        Estimate estimateById = estimateRepository.findEstimateByRequestNumber(reformRequest)
                .orElseThrow(() -> new IllegalStateException("견적서가 존재하지 않습니다."));
        EstimateStatus estimateStatus = estimateById.getEstimateStatus();

        try{
            if(estimateStatus.equals(EstimateStatus.WRITING)){
                estimateById.setEstimateStatus(EstimateStatus.REQUEST_WAITING);
                estimateRepository.save(estimateById);
            }else{
                throw new Exception("작성 상태가 아닙니다.");
            }
        }catch (Exception e){
            throw new Exception("상태 변경에 실패하였습니다.",e);
        }
    }

    /**
     * 디자이너의 견적서 조회
     * @param requestNumber
     * @return
     */
    @Transactional
    public EstimateResponseDTO getEstimate(Long requestNumber) {
        ReformRequest reformRequest = requestRepository.findReformRequestById(requestNumber)
                .orElseThrow(() -> new IllegalStateException("요청서가 존재하지 않습니다."));
        Estimate estimateById = estimateRepository.findEstimateByRequestNumber(reformRequest)
                .orElseThrow(() -> new IllegalStateException("견적서가 존재하지 않습니다."));
        EstimateResponseDTO estimateResponseDTO = new EstimateResponseDTO();
        estimateResponseDTO.setEstimateNumber(estimateById.getId());
        estimateResponseDTO.setClientEmail(estimateById.getPurchaserEmail().getEmail());
        estimateResponseDTO.setDesignerEmail(estimateById.getDesignerEmail().getEmail());
        estimateResponseDTO.setEstimateInfo(estimateById.getEstimateInfo());
        estimateResponseDTO.setTotalPrice(estimateById.getPrice());  // 총 가격
        estimateResponseDTO.setPrice(estimateById.getReformPrice());  // 리폼 가격
        estimateResponseDTO.setEstimateImg(estimateById.getEstimateImg().get(0).getImgUrl());
        estimateResponseDTO.setRequestNumber(estimateById.getRequestNumber().getId());
        estimateResponseDTO.setEstimateStatus(estimateById.getEstimateStatus().name());

        return estimateResponseDTO;
    }

    /**
     * 구매자의 견적서 조회
     * @param requestNumber
     * @return
     */
    @Transactional
    public Optional<EstimateResponseDTO> getPurEstimate(Long requestNumber) {
        ReformRequest reformRequest = requestRepository.findReformRequestById(requestNumber)
                .orElseThrow(() -> new IllegalStateException("요청서가 존재하지 않습니다."));
        Optional<Estimate> estimate = estimateRepository.findByRequestNumberAndEstimateStatusNot(reformRequest, EstimateStatus.WRITING);

        if (!estimate.isPresent()) {
            return Optional.empty();  // 견적서가 없으면 빈 Optional 반환
        }

        // 견적서가 있으면 DTO를 생성하고 반환
        EstimateResponseDTO estimateResponseDTO = convertToDTO(estimate.get());
        return Optional.of(estimateResponseDTO);
    }

    private EstimateResponseDTO convertToDTO(Estimate estimate) {
        EstimateResponseDTO dto = new EstimateResponseDTO();
        dto.setEstimateNumber(estimate.getId());
        dto.setClientEmail(estimate.getPurchaserEmail().getEmail());
        dto.setDesignerEmail(estimate.getDesignerEmail().getEmail());
        dto.setEstimateInfo(estimate.getEstimateInfo());
        dto.setTotalPrice(estimate.getPrice());
        dto.setPrice(estimate.getReformPrice());
        dto.setEstimateImg(estimate.getEstimateImg().get(0).getImgUrl());
        dto.setRequestNumber(estimate.getRequestNumber().getId());
        dto.setEstimateStatus(estimate.getEstimateStatus().name());
        return dto;
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

        if (estimate.getEstimateStatus() != EstimateStatus.WRITING) {
            throw new RuntimeException("이미 진행 중인 의뢰로 수정이 불가합니다.");
        } else {
            List<EstimateImage> imageList = estimateImgRepository.findAllByEstimateId(estimateNumber);
            estimate.setEstimateInfo(estimateRequestDTO.getEstimateInfo());
            estimate.setReformPrice(estimateRequestDTO.getReformPrice());  // 리폼 비용
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
    public void selEstimateAccept(Long estimateNumber) {
        Estimate estimate = estimateRepository.findEstimateById(estimateNumber)
                .orElseThrow(() -> new IllegalArgumentException("견적서가 존재하지 않습니다."));

        if (estimate.getEstimateStatus() != EstimateStatus.REQUEST_WAITING) {
            throw new RuntimeException("이미 진행 중인 의뢰로 수정이 불가합니다.");
        } else {
            ReformOrder reformOrder = new ReformOrder();
            reformOrder.setOrderStatus(ReformOrderStatus.ORDERING);  // 주문 상태 : 주문 중
            reformOrder.setTotalPrice(estimate.getPrice());  // 상품 총 가격
            reformOrder.setEstimate(estimate);
            reformOrderRepository.save(reformOrder);
        }
    }

    @Transactional
    public void selEstimateReject(Long estimateNumber) {
        Estimate estimate = estimateRepository.findEstimateById(estimateNumber)
                .orElseThrow(() -> new IllegalArgumentException("견적서가 존재하지 않습니다."));

        if (estimate.getEstimateStatus() != EstimateStatus.REQUEST_WAITING) {
            throw new RuntimeException("이미 진행 중인 의뢰로 수정이 불가합니다.");
        } else {
            estimate.setEstimateStatus(EstimateStatus.REQUEST_REJECTED);
            estimateRepository.save(estimate);
        }
    }

    @Transactional
    public void acceptOrdering(ReformOrderRequestDTO reformOrderRequestDTO, Long estimateNumber) {

        ReformOrder reformOrder = reformOrderRepository.findReformOrderByEstimateId(estimateNumber)
                .orElseThrow(() -> new IllegalArgumentException("견적서가 존재하지 않습니다."));

        reformOrder.setPhoneNumber(reformOrderRequestDTO.getPhoneNumber());
        reformOrder.setDeliveryRequest(reformOrderRequestDTO.getDeliveryRequest());
        reformOrder.setPostcode(reformOrderRequestDTO.getPostcode());
        reformOrder.setAddress(reformOrderRequestDTO.getAddress());
        reformOrder.setDetailAddress(reformOrderRequestDTO.getDetailAddress());

        reformOrderRepository.save(reformOrder);

    }

    @Transactional
    public ReformOrderResponseDTO getAcceptOrderingPrice(Long estimateNumber) {

        Estimate estimate = estimateRepository.findEstimateById(estimateNumber)
                .orElseThrow(() -> new IllegalArgumentException("견적서가 존재하지 않습니다."));

        ReformOrderResponseDTO reformOrderResponseDTO = new ReformOrderResponseDTO();
        reformOrderResponseDTO.setProductPrice(estimate.getRequestNumber().getProductNumber().getPrice());  // 상품 가격
        reformOrderResponseDTO.setReformPrice(estimate.getReformPrice());  // 리폼 가격
        reformOrderResponseDTO.setTotalPrice(estimate.getReformPrice());  // 총 가격

        return reformOrderResponseDTO;
    }

    /**
     * 구매자의 견적서 수락 최종 완료 시, 형상관리 시작
     * @param estimateNumber
     */
    @Transactional
    public void setAcceptComplete(Long estimateNumber) {
        Estimate estimate = estimateRepository.findEstimateById(estimateNumber)
                .orElseThrow(() -> new IllegalArgumentException("견적서가 존재하지 않습니다."));

        ReformOrder reformOrder = reformOrderRepository.findReformOrderByEstimateId(estimateNumber)
                .orElseThrow(() -> new IllegalArgumentException("견적서가 존재하지 않습니다."));

        ReformRequest reformRequest = requestRepository.findReformRequestById(estimate.getRequestNumber().getId())
                .orElseThrow(() -> new IllegalArgumentException("요청서가 존재하지 않습니다."));

        estimate.setEstimateStatus(EstimateStatus.REQUEST_ACCEPTED);  // 견적서 상태 : 요청 수락
        reformOrder.setOrderStatus(ReformOrderStatus.ORDER_COMPLETE);   // 리폼 주문 상태 : 주문 완료

        estimateRepository.save(estimate);
        reformOrderRepository.save(reformOrder);

        Progressmanagement progressmanagement = new Progressmanagement();  // 형상관리 시작 -> 첫 번째 이미지(상품 사진) 등록

        if (estimate.getEstimateStatus() != EstimateStatus.REQUEST_ACCEPTED) {
            throw new RuntimeException("수락된 의뢰만 형상관리 진행이 가능합니다.");
        } else {
            progressmanagement.setProgressStatus(ProgressStatus.REFORM_START);
            progressmanagement.setProductImgUrl(reformRequest.getProductNumber().getProductImg().get(0).getImgUrl());
            progressmanagement.setClientEmail(estimate.getPurchaserEmail().getEmail());
            progressmanagement.setDesignerEmail(estimate.getDesignerEmail().getEmail());
            progressmanagement.setRequestNumber(reformRequest);
            progressmanagement.setEstimateNumber(estimate);
            progressRepository.save(progressmanagement);
            log.info("형상관리 데이터 생성 완료");
        }

    }


}

