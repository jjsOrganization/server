package com.jjs.ClothingInventorySaleReformPlatform.service.reformrequest;

import com.jjs.ClothingInventorySaleReformPlatform.domain.portfolio.Portfolio;
import com.jjs.ClothingInventorySaleReformPlatform.domain.product.Product;
import com.jjs.ClothingInventorySaleReformPlatform.domain.reformrequest.ReformRequest;
import com.jjs.ClothingInventorySaleReformPlatform.domain.reformrequest.ReformRequestImage;
import com.jjs.ClothingInventorySaleReformPlatform.domain.reformrequest.ReformRequestStatus;
import com.jjs.ClothingInventorySaleReformPlatform.domain.user.DesignerInfo;
import com.jjs.ClothingInventorySaleReformPlatform.domain.user.PurchaserInfo;
import com.jjs.ClothingInventorySaleReformPlatform.domain.user.User;
import com.jjs.ClothingInventorySaleReformPlatform.dto.product.response.ProductListDTO;
import com.jjs.ClothingInventorySaleReformPlatform.dto.reformrequest.ReformProductInfoDTO;
import com.jjs.ClothingInventorySaleReformPlatform.dto.reformrequest.ReformRequestCheckDTO;
import com.jjs.ClothingInventorySaleReformPlatform.dto.reformrequest.ReformRequestCheckPurchaserDTO;
import com.jjs.ClothingInventorySaleReformPlatform.dto.reformrequest.ReformRequestDTO;
import com.jjs.ClothingInventorySaleReformPlatform.repository.auth.DesignerRepository;
import com.jjs.ClothingInventorySaleReformPlatform.repository.auth.PurchaserRepository;
import com.jjs.ClothingInventorySaleReformPlatform.repository.portfolio.PortfolioRepository;
import com.jjs.ClothingInventorySaleReformPlatform.repository.product.ProductImgRepository;
import com.jjs.ClothingInventorySaleReformPlatform.repository.product.ProductRepository;
import com.jjs.ClothingInventorySaleReformPlatform.repository.reformrequest.ReformRequestImgRepository;
import com.jjs.ClothingInventorySaleReformPlatform.repository.reformrequest.ReformRequestRepository;
import com.jjs.ClothingInventorySaleReformPlatform.service.s3.S3Service;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;
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

    @Transactional
    public void saveReformRequest(ReformRequestDTO reformRequestDTO, Long itemId) throws Exception{
        // 구매자와 디자이너의 이메일 삽입을 위한 객체 생성
        PurchaserInfo purchaserInfo = purchaserRepository.findPurchaserByEmail(getCurrentUsername());
        DesignerInfo designerInfo = designerRepository.findDesignerByEmail(reformRequestDTO.getDesignerEmail());

        // 의뢰서 등록
        ReformRequest reformRequest = new ReformRequest();
        reformRequest.setRequestInfo(reformRequestDTO.getRequestInfo());
        reformRequest.setRequestPart(reformRequestDTO.getRequestPart());
        reformRequest.setRequestPrice(reformRequestDTO.getRequestPrice());
        reformRequest.setRequestStatus(ReformRequestStatus.REQUEST_WAITING);

        reformRequest.setClientEmail(purchaserInfo);
        reformRequest.setDesignerEmail(designerInfo);
        reformRequest.setProductNumber(productRepository.findProductById(itemId));
        reformRequestRepository.save(reformRequest);

        // 첨부 이미지 등록
        saveImageList(reformRequestDTO,reformRequest);

    }

    @Transactional
    public ReformRequestDTO getReformRequestForm(Long requestId) {
        ReformRequest reformRequestById = reformRequestRepository.findReformRequestById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("의뢰서가 존재하지 않습니다."));

        ReformRequestDTO reformRequestDTO = new ReformRequestDTO();
        reformRequestDTO.setRequestInfo(reformRequestById.getRequestInfo());
        reformRequestDTO.setRequestPrice(reformRequestById.getRequestPrice());
        reformRequestDTO.setRequestPart(reformRequestById.getRequestPart());

        return reformRequestDTO;
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

    @Transactional
    public void saveImageList(ReformRequestDTO reformRequestDTO, ReformRequest reformRequest) throws IOException {// 의뢰서 수정 이미지 저장
        reformRequestDTO.getRequestImg().forEach(requestImage -> {
            try {
                ReformRequestImage reformRequestImage = new ReformRequestImage();
                reformRequestImage.setImgUrl(s3Service.uploadFile(requestImage, "ReformRequestImage/"));
                reformRequestImage.setReformRequest(reformRequest);
                reformRequestImgRepository.save(reformRequestImage);

            } catch (IOException e) {
                log.error(e.getMessage());
                throw new RuntimeException("이미지가 없습니다.");
            }
        });
    }
/*
    public List<ReformRequestCheckDTO> getAllRequestList() {
        PurchaserInfo purchaserInfo = new PurchaserInfo();
        purchaserInfo.setEmail(getCurrentUsername());

        List<ReformRequest> reformRequestsByPurchaserEmail = reformRequestRepository.findReformRequestsByClientEmail(purchaserInfo)
                .orElseThrow(() -> new IllegalArgumentException("요청받은 의뢰가 없습니다."));

        List<ReformRequestCheckDTO> reformRequestCheckDTOList = reformRequestsByPurchaserEmail.stream() // 의뢰서 엔티티 -> DTO로 변경 후 반환
                .map(ReformRequestCheckDTO::convertToDTO) // 2차원 배열 형태로 의뢰서 데이터가 들어옴. 각 배열에 대해 convert 적용
                .collect(Collectors.toList());

        return reformRequestCheckDTOList;
    }

 */
    public List<ReformRequestCheckPurchaserDTO> getAllRequestList() {
        String currentUsername = getCurrentUsername();
        /*
        PurchaserInfo purchaserInfo = new PurchaserInfo();
        purchaserInfo.setEmail(getCurrentUsername());
        User user = new User();
        user.setEmail(getCurrentUsername());

         */

        List<ReformRequest> reformRequestsByPurchaserEmail = reformRequestRepository.findByClientEmail_Email(currentUsername);
                //.orElseThrow(() -> new IllegalArgumentException("요청받은 의뢰가 없습니다."));

        return reformRequestsByPurchaserEmail.stream().map(request -> {
            Portfolio portfolio = portfolioRepository.findByDesignerEmail_Email(request.getDesignerEmail().getEmail()).orElseThrow(() -> new EntityNotFoundException("Portfolio not found"));
            return ReformRequestCheckPurchaserDTO.convertToDTO(request, portfolio);
        }).collect(Collectors.toList());


    }



}
