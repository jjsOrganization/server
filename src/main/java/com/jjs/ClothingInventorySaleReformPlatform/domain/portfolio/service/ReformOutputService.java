package com.jjs.ClothingInventorySaleReformPlatform.domain.portfolio.service;

import com.jjs.ClothingInventorySaleReformPlatform.domain.portfolio.dto.request.ReformOutputDTO;
import com.jjs.ClothingInventorySaleReformPlatform.domain.portfolio.dto.request.ReformPeriodDTO;
import com.jjs.ClothingInventorySaleReformPlatform.domain.portfolio.dto.response.EditContentsReformOutputDTO;
import com.jjs.ClothingInventorySaleReformPlatform.domain.portfolio.dto.response.FixedReformOutputDTO;
import com.jjs.ClothingInventorySaleReformPlatform.domain.portfolio.dto.response.ReformOutputDetailDTO;
import com.jjs.ClothingInventorySaleReformPlatform.domain.portfolio.dto.response.ReformOutputListDTO;
import com.jjs.ClothingInventorySaleReformPlatform.domain.portfolio.entity.Portfolio;
import com.jjs.ClothingInventorySaleReformPlatform.domain.portfolio.entity.ReformOutput;
import com.jjs.ClothingInventorySaleReformPlatform.domain.portfolio.repository.PortfolioRepository;
import com.jjs.ClothingInventorySaleReformPlatform.domain.portfolio.repository.ReformOutputRepository;
import com.jjs.ClothingInventorySaleReformPlatform.domain.product.dto.response.ProductListDTO;
import com.jjs.ClothingInventorySaleReformPlatform.domain.product.entity.Product;
import com.jjs.ClothingInventorySaleReformPlatform.domain.product.entity.ProductLikeCount;
import com.jjs.ClothingInventorySaleReformPlatform.domain.reform.entity.estimate.Estimate;
import com.jjs.ClothingInventorySaleReformPlatform.domain.reform.entity.progressManagement.Progressmanagement;
import com.jjs.ClothingInventorySaleReformPlatform.domain.reform.entity.reformRequest.ReformRequest;
import com.jjs.ClothingInventorySaleReformPlatform.domain.reform.repository.ProgressRepository;
import com.jjs.ClothingInventorySaleReformPlatform.domain.reform.repository.ReformRequestRepository;
import com.jjs.ClothingInventorySaleReformPlatform.global.common.authentication.AuthenticationFacade;
import com.jjs.ClothingInventorySaleReformPlatform.global.s3.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.core.Local;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReformOutputService {

    private final PortfolioRepository portfolioRepository;
    private final S3Service s3Service;
    private final AuthenticationFacade authenticationFacade;
    private final ReformRequestRepository reformRequestRepository;
    private final ProgressRepository progressRepository;
    private final ReformOutputRepository reformOutputRepository;

    private String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getName();
        }
        return null;
    }

    /**
     * 디자이너 포트폴리오 작업물 등록
     * @param reformOutputDTO 제목, 내용 직접 입력, 나머지는 자동 기입
     * @param progressNumber  형상관리 id에 대하여 생성됨
     */
    public void saveReformOutput(ReformOutputDTO reformOutputDTO, Long progressNumber) {

        Progressmanagement progress = progressRepository.findById(progressNumber)
                .orElseThrow(() -> new IllegalArgumentException("progressId에 해당되는 형상관리 없음"));
        ReformRequest reformRequest = progress.getRequestNumber();  // 요청서
        Estimate estimate = progress.getEstimateNumber();  // 견적서
        String designerEmail = getCurrentUsername();
        Portfolio portfolio = portfolioRepository.findByDesignerEmail_Email(designerEmail)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이메일입니다."));
        ReformOutput reformOutput = new ReformOutput();

        LocalDate startDate = reformRequest.getRegDate().toLocalDate();
        LocalDate endDate = progress.getUpdateDate().toLocalDate();

        reformOutput.setName(portfolio.getName());  // 디자이너명
        reformOutput.setProductImgUrl(progress.getProductImgUrl());  // 초기 사진(상품 사진 → 형상관리1)
        reformOutput.setReformRequestImgUrl(String.valueOf(reformRequest.getReformRequestImageList().get(0).getImgUrl()));  // 구매자 요청 사진(요청서)
        reformOutput.setEstimateImgUrl(String.valueOf(estimate.getEstimateImg().get(0).getImgUrl()));  // 디자이너 견적 사진(견적서)
        reformOutput.setCompleteImgUrl(progress.getCompleteImgUrl());  // 완성 사진(형상관리4)
        reformOutput.setWorkingPeriod(reformOutput.calWorkingPeriod(startDate, endDate));  // 리폼 기간(일수→시작일: 요청서, 완료일: 형상관리 마지막 업데이트)
        reformOutput.setPrice(estimate.getPrice());  // 리폼 가격(견적서)
        reformOutput.setCategory(reformRequest.getProductNumber().getCategory().getCategoryName());  // 리폼 부위(요청서)
        reformOutput.setProgress(progress);

        reformOutput.setTitle(reformOutputDTO.getTitle());  // 제목(직접 입력)
        reformOutput.setExplanation(reformOutputDTO.getExplanation());  // 내용(직접 입력)

        reformOutputRepository.save(reformOutput);

    }

    /**
     * 포트폴리오 작업물 등록 및 수정 시, 고정 항목 조회
     * @param progressNumber 형상관리 id
     * @return fixedReformOutputDTO
     */
    public FixedReformOutputDTO getReformOutputByProgressId(Long progressNumber) {
        Progressmanagement progress = progressRepository.findById(progressNumber)
                .orElseThrow(() -> new IllegalArgumentException("progressId에 해당되는 형상관리 없음"));
        ReformRequest reformRequest = progress.getRequestNumber();  // 요청서
        Estimate estimate = progress.getEstimateNumber();  // 견적서
        String designerEmail = getCurrentUsername();
        Portfolio portfolio = portfolioRepository.findByDesignerEmail_Email(designerEmail)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이메일입니다."));

        FixedReformOutputDTO fixedReformOutputDTO = new FixedReformOutputDTO();
        fixedReformOutputDTO.setDesignerName(portfolio.getName());
        fixedReformOutputDTO.setProductName(reformRequest.getProductNumber().getProductName());
        fixedReformOutputDTO.setDesignerProfileImg(portfolio.getDesignerImage());
        fixedReformOutputDTO.setReformRequestInfo(reformRequest.getRequestInfo());
        fixedReformOutputDTO.setEstimateInfo(estimate.getEstimateInfo());
        fixedReformOutputDTO.setProductImgUrl(progress.getProductImgUrl());
        fixedReformOutputDTO.setReformRequestImgUrl(reformRequest.getReformRequestImageList().get(0).getImgUrl());
        fixedReformOutputDTO.setEstimateImgUrl(String.valueOf(estimate.getEstimateImg().get(0).getImgUrl()));
        fixedReformOutputDTO.setCompleteImgUrl(progress.getCompleteImgUrl());
        fixedReformOutputDTO.setCategory(reformRequest.getProductNumber().getCategory().getCategoryName());
        fixedReformOutputDTO.setPrice(estimate.getPrice());

        LocalDate startDate = reformRequest.getRegDate().toLocalDate();
        LocalDate endDate = progress.getUpdateDate().toLocalDate();
        ReformOutput reformOutput = new ReformOutput();
        fixedReformOutputDTO.setWorkingPeriod(reformOutput.calWorkingPeriod(startDate, endDate));

        return fixedReformOutputDTO;
    }

    public EditContentsReformOutputDTO getEditContentsByProgressId(Long progressNumber) {
        ReformOutput reformOutput = reformOutputRepository.findByProgress_id(progressNumber)
                .orElseThrow(() -> new IllegalArgumentException("progressId에 해당되는 작업물 없음"));

        EditContentsReformOutputDTO editContentsReformOutputDTO = new EditContentsReformOutputDTO();
        editContentsReformOutputDTO.setTitle(reformOutput.getTitle());
        editContentsReformOutputDTO.setExplanation(reformOutput.getExplanation());

        return editContentsReformOutputDTO;
    }

    public void editReformOutput(ReformOutputDTO reformOutputDTO, Long progressNumber) {
        ReformOutput reformOutput = reformOutputRepository.findByProgress_id(progressNumber)
                .orElseThrow(() -> new IllegalArgumentException("progressId에 해당되는 작업물 없음"));

        reformOutput.setTitle(reformOutputDTO.getTitle());
        reformOutput.setExplanation(reformOutputDTO.getExplanation());

        reformOutputRepository.save(reformOutput);
    }

    public Optional<ReformOutputDetailDTO> getReformOutput(Long progressNumber) {
        ReformOutput reformOutput = reformOutputRepository.findByProgress_id(progressNumber)
                .orElseThrow(() -> new IllegalArgumentException("progressId에 해당되는 작업물 없음"));

        ReformOutputDetailDTO reformOutputDetailDTO = new ReformOutputDetailDTO();
        reformOutputDetailDTO.setDesignerName(reformOutput.getName());
        reformOutputDetailDTO.setTitle(reformOutput.getTitle());
        reformOutputDetailDTO.setDate(reformOutput.getRegDate().toLocalDate());

        reformOutputDetailDTO.setProductImgUrl(reformOutput.getProductImgUrl());
        reformOutputDetailDTO.setReformRequestImgUrl(reformOutput.getReformRequestImgUrl());
        reformOutputDetailDTO.setEstimateImgUrl(reformOutput.getEstimateImgUrl());
        reformOutputDetailDTO.setCompleteImgUrl(reformOutput.getCompleteImgUrl());

        reformOutputDetailDTO.setWorkingPeriod(reformOutput.getWorkingPeriod());
        reformOutputDetailDTO.setCategory(reformOutput.getCategory());
        reformOutputDetailDTO.setPrice(reformOutput.getPrice());
        reformOutputDetailDTO.setExplanation(reformOutput.getExplanation());

        return Optional.of(reformOutputDetailDTO);
    }

    public List<ReformOutputListDTO> getAllReformOutputs() {
        List<ReformOutput> reformOutputs = reformOutputRepository.findAll();
        return reformOutputs.stream()
                .map(ReformOutputListDTO::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ReformOutputListDTO> getLoginReformOutputs(String createBy) {
        List<ReformOutput> reformOutputs = reformOutputRepository.findByCreateBy(createBy);
        return reformOutputs.stream()
                .map(ReformOutputListDTO::convertToDTO)
                .collect(Collectors.toList());
    }
}
