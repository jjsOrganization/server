package com.jjs.ClothingInventorySaleReformPlatform.domain.reform.service;

import com.jjs.ClothingInventorySaleReformPlatform.domain.portfolio.entity.Portfolio;
import com.jjs.ClothingInventorySaleReformPlatform.domain.product.entity.Product;
import com.jjs.ClothingInventorySaleReformPlatform.domain.product.entity.ProductImg;
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
import org.springframework.util.StringUtils;

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
    private final PortfolioRepository portfolioRepository;
    private final EstimateRepository estimateRepository;

    private String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getName();
        }
        return null;
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

        return reformRequestById.toDTO();
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

    // 테스트하려면 Repository에서 아이디로 요청서 가져와서 변경후 Assertions.assertThat으로 비교?
    @Transactional
    public void updateReformRequest(ReformRequestDTO reformRequestDTO, Long requestId) throws IOException {

        ReformRequest reformRequestById = reformRequestRepository.findReformRequestById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("의뢰서가 존재하지 않습니다."));

        if(reformRequestById.getRequestStatus() != ReformRequestStatus.REQUEST_WAITING){ // 여기서 하는게 맞는지...?
            throw new IllegalStateException("이미 진행중인 의뢰입니다.");
        }else{

            // set 메소드로 값 세팅 -> update 메소드 호출로 변환
            // 엔티티가 DTO를 의존하지 않게 아래처럼 파라미터로 직접 값을 넘기게 작성함
            // 서비스 레이어에서는 메소드를 실행하는 역할만 하려고 하는거로 이해하고 있는데,
            // 이렇게 리팩토링 하는게 맞는지..?
            reformRequestById.updateReformRequestInfo(
                    reformRequestDTO.getRequestPart()
                    , reformRequestDTO.getRequestPrice()
                    , reformRequestDTO.getRequestInfo());

//            reformRequestRepository.save(reformRequestById); 더티 체킹으로 체크

            // 수정할 리폼 의뢰서 이미지 list 가져옴
            List<ReformRequestImage> requestImgList = reformRequestImgRepository.findAllByReformRequestId(requestId);

            for (ReformRequestImage requestImage : requestImgList) { // 기존 의뢰서 이미지 삭제
                if(StringUtils.hasText(requestImage.getImgUrl())){ // 이미지 유효성 검사 로직 변경
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
//                reformRequestImgRepository.save(reformRequestImage);

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

//    /**
//     * 리폼 의뢰 맡기는 상품 정보 가져오기
//     * @param itemId
//     * @return
//     */
//    @Transactional
//    public ReformProductInfoDTO getProductInfo(Long itemId) {
//        Product findProduct = productRepository.findProductById(itemId);
//        List<ProductImg> productImgs = productImgRepository.findByProductId(itemId);
//
//        return new ReformProductInfoDTO(itemId, findProduct,productImgs);
//    }
}
