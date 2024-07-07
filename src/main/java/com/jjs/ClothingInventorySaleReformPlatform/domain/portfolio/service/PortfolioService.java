package com.jjs.ClothingInventorySaleReformPlatform.domain.portfolio.service;

import com.jjs.ClothingInventorySaleReformPlatform.domain.user.repository.UserRepository;
import com.jjs.ClothingInventorySaleReformPlatform.global.common.authentication.AuthenticationFacade;
import com.jjs.ClothingInventorySaleReformPlatform.domain.portfolio.entity.Portfolio;
import com.jjs.ClothingInventorySaleReformPlatform.domain.user.entity.User;
import com.jjs.ClothingInventorySaleReformPlatform.domain.portfolio.dto.PortfolioDTO;
import com.jjs.ClothingInventorySaleReformPlatform.domain.portfolio.dto.PortfolioInfoDTO;
import com.jjs.ClothingInventorySaleReformPlatform.domain.portfolio.repository.PortfolioRepository;
import com.jjs.ClothingInventorySaleReformPlatform.domain.portfolio.repository.mapping.ImageUrlMapping;
import com.jjs.ClothingInventorySaleReformPlatform.global.s3.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PortfolioService {

    private final PortfolioRepository portfolioRepository;
    private final S3Service s3Service;
    private final AuthenticationFacade authenticationFacade;

    private String imageUploadPath = "PortfolioImages/";
    private String priceImageUploadPath = "PortfolioImages/price/";


    private String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getName();
        }
        return null;
    }

    /**
     *
     * portfolio 저장 메소드
     * @param portfolioDTO(포트폴리오 입력 정보)
     * @throws IOException
     */
    @Transactional
    public String savePortfolio(PortfolioDTO portfolioDTO) throws IOException {
        Portfolio portfolio = portfolioDTO.toEntity(portfolioDTO);

        String userEmail = getCurrentUsername(); // 서비스 레이어에서는 현재 유저의 이메일만 파라미터로 제공하면 됨.
        // 이전에 User 객체를 생성해서 이메일을 set 해서 DesignerEmail에 set 했다면 현재는 이메일만 제공하면 엔티티 레이어에서 값 세팅하는 로직 수행

        // 이메일 이미 존재 할 경우
        if (portfolioRepository.findByDesignerEmail_Email(userEmail).isPresent()) {
            throw new RuntimeException("이미 존재하는 이메일입니다.");
        }
        else{
            String storedDesignerImageUrl = s3Service.uploadFile(portfolioDTO.getDesignerImage(),imageUploadPath); // s3에 저장한 이미지의 URL 반환
            String storedPriceImageUrl = s3Service.uploadFile(portfolioDTO.getPriceImage(), priceImageUploadPath); // s3에 저장한 가격표 이미지 URL 반환

            portfolio.setPortfolio(userEmail , storedDesignerImageUrl, storedPriceImageUrl);
            portfolioRepository.save(portfolio);

            return portfolio.getDesignerEmail().getEmail();
        }
    }

    @Transactional
    public void updatePortfolio(PortfolioDTO portfolioDTO) throws IOException {
        Portfolio portfolio = portfolioDTO.toEntity(portfolioDTO);

        String userEmail = getCurrentUsername(); // 서비스 레이어에서는 현재 유저의 이메일만 파라미터로 제공하면 됨.
        // 이전에 User 객체를 생성해서 이메일을 set 해서 DesignerEmail에 set 했다면 현재는 이메일만 제공하면 엔티티 레이어에서 값 세팅하는 로직 수행

        ImageUrlMapping imageUrlById = portfolioRepository.findPortfolioById(portfolioDTO.getID()) // 저장되어있는 이미지의 URL 반환
                .orElseThrow(() -> new IllegalArgumentException("이미지가 존재하지 않습니다."));

        s3Service.fileDelete(imageUrlById.getDesignerImage()); // 저장된 이미지 삭제

        String storedDesignerImageUrl = s3Service.uploadFile(portfolioDTO.getDesignerImage(),imageUploadPath); // s3에 저장한 이미지의 URL 반환
        String storedPriceImageUrl = s3Service.uploadFile(portfolioDTO.getPriceImage(), priceImageUploadPath); // s3에 저장한 가격표 이미지 URL 반환

        // portfolio 값 set
        portfolio.setPortfolio(userEmail, storedDesignerImageUrl, storedPriceImageUrl);
        portfolioRepository.save(portfolio);
    }

    /**
     * Repository에서 포트폴리오 정보 조회 메소드
     * @param  -> String으로 입력 받으나, 엔티티 타입으로 선언되어 있기에 User 타입으로 변환하여 사용해야 함.
     * @return
     */
    @Transactional
    public Optional<PortfolioInfoDTO> getPortfolio(){

        String designerEmail = authenticationFacade.getCurrentUsername(); // 로그인되어 있는 유저의 이메일

        Portfolio portfolioByDesignerEmail = (portfolioRepository.findByDesignerEmail_Email(designerEmail))
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이메일입니다."));

        if(portfolioByDesignerEmail != null){
            return Optional.of(Portfolio.convertToDTO(portfolioByDesignerEmail));
        }else{
            throw new RuntimeException("포트폴리오가 존재하지 않습니다.");
        }
    }


    /**
     * 모든 디자이너 정보 검색 메소드
     * @return 디자이너 정보 List
     * @throws IOException
     */

    @Transactional
    public List<PortfolioInfoDTO> getAllPortfolio() throws IOException{
        return portfolioRepository.findAll()
                .stream()
                .map(Portfolio::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * 키워드로 디자이너 검색 메소드
     * @param keyword
     * @return 검색된 디자이너 정보 List
     */
    @Transactional
    public List<PortfolioInfoDTO> getPortfolioByName(String keyword) {
        Optional<List<Portfolio>> optionalPortfolios = portfolioRepository.findByNameContaining(keyword);
        List<Portfolio> findPortfolioList = optionalPortfolios.orElse(Collections.emptyList());

        return findPortfolioList
                .stream()
                .map(Portfolio::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<Portfolio> getPortfolioById(Long id) {
        return portfolioRepository.findById(id);
    }

}
