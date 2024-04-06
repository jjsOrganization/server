package com.jjs.ClothingInventorySaleReformPlatform.service;

import com.jjs.ClothingInventorySaleReformPlatform.domain.reform.ProgressStatus;
import com.jjs.ClothingInventorySaleReformPlatform.domain.reform.Progressmanagement;
import com.jjs.ClothingInventorySaleReformPlatform.domain.reform.estimate.Estimate;
import com.jjs.ClothingInventorySaleReformPlatform.domain.reform.estimate.EstimateStatus;
import com.jjs.ClothingInventorySaleReformPlatform.domain.reform.reformrequest.ReformRequest;
import com.jjs.ClothingInventorySaleReformPlatform.repository.ProgressRepository;
import com.jjs.ClothingInventorySaleReformPlatform.repository.estimate.EstimateRepository;
import com.jjs.ClothingInventorySaleReformPlatform.repository.reformrequest.ReformRequestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProgressManagementService {

    private final EstimateRepository estimateRepository;
    private final ProgressRepository progressRepository;
    private final ReformRequestRepository requestRepository;


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
            progressmanagement.setFirstImgUrl(reformRequest.getProductNumber().getProductImg().get(0).getImgUrl());
            progressmanagement.setClientEmail(estimate.getClientEmail().getEmail());
            progressmanagement.setDesignerEmail(estimate.getDesignerEmail().getEmail());
            progressmanagement.setRequestNumber(reformRequest);
            progressmanagement.setEstimateNumber(estimate);
            progressRepository.save(progressmanagement);
            System.out.println("44444");
        }
    }
}
