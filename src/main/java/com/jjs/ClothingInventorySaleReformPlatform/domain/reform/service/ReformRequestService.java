package com.jjs.ClothingInventorySaleReformPlatform.domain.reform.service;

import com.jjs.ClothingInventorySaleReformPlatform.domain.portfolio.entity.Portfolio;
import com.jjs.ClothingInventorySaleReformPlatform.domain.product.entity.Product;
import com.jjs.ClothingInventorySaleReformPlatform.domain.reform.dto.response.GetEstimateNumberResponseDTO;
import com.jjs.ClothingInventorySaleReformPlatform.domain.reform.entity.estimate.Estimate;
import com.jjs.ClothingInventorySaleReformPlatform.domain.reform.entity.reformRequest.ReformRequest;
import com.jjs.ClothingInventorySaleReformPlatform.domain.reform.entity.reformRequest.ReformRequestImage;
import com.jjs.ClothingInventorySaleReformPlatform.domain.reform.entity.reformRequest.ReformRequestStatus;
import com.jjs.ClothingInventorySaleReformPlatform.domain.reform.repository.EstimateRepository;
import com.jjs.ClothingInventorySaleReformPlatform.domain.user.entity.DesignerInfo;
import com.jjs.ClothingInventorySaleReformPlatform.domain.user.entity.PurchaserInfo;
import com.jjs.ClothingInventorySaleReformPlatform.domain.reform.dto.response.ReformProductInfoDTO;
import com.jjs.ClothingInventorySaleReformPlatform.domain.reform.dto.response.ReformRequestCheckPurchaserDTO;
import com.jjs.ClothingInventorySaleReformPlatform.domain.reform.dto.request.ReformRequestDTO;
import com.jjs.ClothingInventorySaleReformPlatform.domain.user.repository.DesignerRepository;
import com.jjs.ClothingInventorySaleReformPlatform.domain.user.repository.PurchaserRepository;
import com.jjs.ClothingInventorySaleReformPlatform.domain.portfolio.repository.PortfolioRepository;
import com.jjs.ClothingInventorySaleReformPlatform.domain.product.repository.ProductImgRepository;
import com.jjs.ClothingInventorySaleReformPlatform.domain.product.repository.ProductRepository;
import com.jjs.ClothingInventorySaleReformPlatform.domain.reform.repository.ReformRequestImgRepository;
import com.jjs.ClothingInventorySaleReformPlatform.domain.reform.repository.ReformRequestRepository;
import com.jjs.ClothingInventorySaleReformPlatform.global.s3.S3Service;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReformRequestService {
    private final S3Service s3Service;

    private final ReformRequestRepository reformRequestRepository;
    private final ReformRequestImgRepository reformRequestImgRepository;

    private final ProductRepository productRepository;
    private final PurchaserRepository purchaserRepository;
    private final DesignerRepository designerRepository;
    private final ProductImgRepository productImgRepository;
    private final PortfolioRepository portfolioRepository;
    private final EstimateRepository estimateRepository;

    private String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getName();
        }
        return null;
    }

    @Transactional
    public ReformProductInfoDTO getProductInfo(Long itemId) {
        ReformProductInfoDTO reformProductInfoDTO = new ReformProductInfoDTO();

        Product productById = productRepository.findProductById(itemId);

        reformProductInfoDTO.setId(itemId);
        reformProductInfoDTO.setProductName(productById.getProductName());
        reformProductInfoDTO.setProductImg(productImgRepository.findByProductId(itemId));
        reformProductInfoDTO.setPrice(productById.getPrice());

        return reformProductInfoDTO;
    }

    /**
     * 리폼 요청서 저장 및 첨부 이미지 저장
     */
    @Transactional
    public void saveReformRequest(ReformRequestDTO reformRequestDTO, Long itemId) throws Exception{

        // 구매자와 디자이너의 이메일 삽입을 위한 객체 생성
        PurchaserInfo purchaserInfo = purchaserRepository.findPurchaserByEmail(getCurrentUsername());
        DesignerInfo designerInfo = designerRepository.findDesignerByEmail(reformRequestDTO.getDesignerEmail());
        Product productNumber = productRepository.findProductById(itemId);

        // 의뢰서 등록
        ReformRequest reformRequest = reformRequestDTO.toEntity(purchaserInfo,designerInfo,productNumber);
        reformRequestRepository.save(reformRequest);

        // 첨부 이미지 등록
        saveImageList(reformRequestDTO,reformRequest);
    }

    @Transactional
    public ReformRequestDTO getReformRequestForm(Long requestId) {
        ReformRequest reformRequestById = reformRequestRepository.findReformRequestById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("의뢰서가 존재하지 않습니다."));

        ReformRequestDTO reformRequestDTO = new ReformRequestDTO();
        reformRequestDTO.setRequestInfo(reformRequestById.getRequestInfo()); // 이런 부분에 대해서도 생성자를 만들어서 사용하는게 나은지?
        reformRequestDTO.setRequestPrice(reformRequestById.getRequestPrice());
        reformRequestDTO.setRequestPart(reformRequestById.getRequestPart());

        return reformRequestDTO;
    }

    @Transactional
    public GetEstimateNumberResponseDTO getEstimateNumber(Long requestNumber) {
        ReformRequest reformRequest = reformRequestRepository.findReformRequestById(requestNumber)
                .orElseThrow(() -> new IllegalArgumentException("의뢰서가 존재하지 않습니다."));
        Estimate estimate = estimateRepository.findEstimateByRequestNumber(reformRequest)
                .orElseThrow(() -> new IllegalArgumentException("견적서가 존재하지 않습니다."));

        GetEstimateNumberResponseDTO getEstimateNumberResponseDTO = new GetEstimateNumberResponseDTO();
        getEstimateNumberResponseDTO.setEstimateNumber(estimate.getId());

        return getEstimateNumberResponseDTO;
    }

    @Transactional
    public void updateReformRequest(ReformRequestDTO reformRequestDTO, Long requestId) throws IOException {

        ReformRequest reformRequestById = reformRequestRepository.findReformRequestById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("의뢰서가 존재하지 않습니다."));

        if(reformRequestById.getRequestStatus() != ReformRequestStatus.REQUEST_WAITING){ // 여기서 하는게 맞는지...?
            throw new RuntimeException("이미 진행중인 의뢰입니다.");
        }else{
            // 수정할 리폼 의뢰서 정보 가져옴
            List<ReformRequestImage> requestImgList = reformRequestImgRepository.findAllByReformRequestId(requestId);

            // entity 레이어에 메소드로 추출필요
            reformRequestById.setId(requestId); // 의뢰서 수정
            reformRequestById.setRequestPart(reformRequestDTO.getRequestPart());
            reformRequestById.setRequestPrice(reformRequestDTO.getRequestPrice());
            reformRequestById.setRequestInfo(reformRequestDTO.getRequestInfo());
            reformRequestRepository.save(reformRequestById);

            for (ReformRequestImage requestImage : requestImgList) { // 기존 의뢰서 이미지 삭제
                if(!requestImage.getImgUrl().isEmpty() && requestImage.getImgUrl() != null){ // 이미지 유효성 검사
                    s3Service.fileDelete(requestImage.getImgUrl());
                    reformRequestImgRepository.deleteByReformRequestId(requestImage.getReformRequest().getId());
                }
            }
            saveImageList(reformRequestDTO, reformRequestById); // 수정 이미지 저장
        }

    }

    /**
     * 요청서에 첨부되는 이미지 저장
     */
    @Transactional
    public void saveImageList(ReformRequestDTO reformRequestDTO, ReformRequest reformRequest) throws IOException {// 의뢰서 수정 이미지 저장
        reformRequestDTO.getRequestImg().forEach(requestImage -> {
            try {
                ReformRequestImage reformRequestImage = new ReformRequestImage();
                String imgPath = s3Service.uploadFile(requestImage, "ReformRequestImage/"); // 이미지 저장한 경로

                reformRequestImage.setReformRequestImage(reformRequest, imgPath);
                reformRequestImgRepository.save(reformRequestImage);

            } catch (IOException e) {
                log.error(e.getMessage());
                throw new RuntimeException("이미지가 없습니다.");
            }
        });
    }

    // 구매자가 요청한 의뢰 내역 전체 조회
    @Transactional
    public List<ReformRequestCheckPurchaserDTO> getAllRequestList() {
        PurchaserInfo purchaserEmail = new PurchaserInfo();
        purchaserEmail.setEmail(getCurrentUsername());

        List<ReformRequest> reformRequestsByPurchaserEmail = reformRequestRepository.findReformRequestsByPurchaserEmail(purchaserEmail);

        return reformRequestsByPurchaserEmail.stream().map(request -> {
            Portfolio portfolio = portfolioRepository.findByDesignerEmail_Email(request.getDesignerEmail().getEmail()).orElseThrow(() -> new EntityNotFoundException("Portfolio not found"));
            return ReformRequestCheckPurchaserDTO.convertToDTO(request, portfolio);
        }).collect(Collectors.toList());
    }


}
