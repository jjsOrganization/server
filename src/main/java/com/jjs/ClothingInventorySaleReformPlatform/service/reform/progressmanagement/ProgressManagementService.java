package com.jjs.ClothingInventorySaleReformPlatform.service.reform.progressmanagement;

import com.jjs.ClothingInventorySaleReformPlatform.domain.reform.progressmanagement.ProgressStatus;
import com.jjs.ClothingInventorySaleReformPlatform.domain.reform.progressmanagement.Progressmanagement;
import com.jjs.ClothingInventorySaleReformPlatform.domain.reform.estimate.Estimate;
import com.jjs.ClothingInventorySaleReformPlatform.domain.reform.estimate.EstimateStatus;
import com.jjs.ClothingInventorySaleReformPlatform.domain.reform.reformrequest.ReformRequest;
import com.jjs.ClothingInventorySaleReformPlatform.dto.reform.request.ProgressImgRequestDTO;
import com.jjs.ClothingInventorySaleReformPlatform.repository.reform.progressmanagement.ProgressRepository;
import com.jjs.ClothingInventorySaleReformPlatform.repository.reform.estimate.EstimateRepository;
import com.jjs.ClothingInventorySaleReformPlatform.repository.reform.reformrequest.ReformRequestRepository;
import com.jjs.ClothingInventorySaleReformPlatform.service.s3.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProgressManagementService {

    private final S3Service s3Service;

    private final EstimateRepository estimateRepository;
    private final ProgressRepository progressRepository;
    private final ReformRequestRepository requestRepository;

    private String imageUploadPath = "ProgressManagement/";


    /**
     * 구매자의 견적서 수락 시, 형상관리 시작
     * @param estimateNumber
     */
    @Transactional
    public void setProgressStart(Long estimateNumber) {
        Estimate estimate = estimateRepository.findEstimateById(estimateNumber)
                .orElseThrow(() -> new IllegalArgumentException("견적서가 존재하지 않습니다1."));

        Progressmanagement progressmanagement = new Progressmanagement();

        ReformRequest reformRequest = requestRepository.findReformRequestById(estimate.getRequestNumber().getId())
                .orElseThrow(() -> new IllegalArgumentException("요청서가 존재하지 않습니다."));

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
            System.out.println("44444");
        }
    }

    @Transactional
    public void saveFirstImg(ProgressImgRequestDTO imgRequestDTO) throws IOException {
        Progressmanagement progressmanagement = progressRepository.findByEstimateNumber_Id(imgRequestDTO.getEstimateId())
                .orElseThrow(() -> new IllegalArgumentException("해당되는 형상관리 정보가 없습니다."));

        progressmanagement.setFirstImgUrl(s3Service.uploadFile(imgRequestDTO.getImgUrl(), imageUploadPath));
        progressmanagement.setProgressStatus(ProgressStatus.REFORMING);
        progressRepository.save(progressmanagement);
    }

    @Transactional
    public void saveSecondImg(ProgressImgRequestDTO imgRequestDTO) throws IOException {
        Progressmanagement progressmanagement = progressRepository.findByEstimateNumber_Id(imgRequestDTO.getEstimateId())
                .orElseThrow(() -> new IllegalArgumentException("해당되는 형상관리 정보가 없습니다."));

        progressmanagement.setSecondImgUrl(s3Service.uploadFile(imgRequestDTO.getImgUrl(), imageUploadPath));
        progressRepository.save(progressmanagement);
    }

    @Transactional
    public void saveCompleteImg(ProgressImgRequestDTO imgRequestDTO) throws IOException {
        Progressmanagement progressmanagement = progressRepository.findByEstimateNumber_Id(imgRequestDTO.getEstimateId())
                .orElseThrow(() -> new IllegalArgumentException("해당되는 형상관리 정보가 없습니다."));

        progressmanagement.setCompleteImgUrl(s3Service.uploadFile(imgRequestDTO.getImgUrl(), imageUploadPath));
        progressmanagement.setProgressStatus(ProgressStatus.REFORM_COMPLETE);
        progressRepository.save(progressmanagement);
    }
}
