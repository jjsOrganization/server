package com.jjs.ClothingInventorySaleReformPlatform.service.designer;

import com.jjs.ClothingInventorySaleReformPlatform.domain.Portfolio;
import com.jjs.ClothingInventorySaleReformPlatform.domain.User;
import com.jjs.ClothingInventorySaleReformPlatform.dto.designer.PortfolioDTO;
import com.jjs.ClothingInventorySaleReformPlatform.dto.designer.PortfolioInfoDTO;
import com.jjs.ClothingInventorySaleReformPlatform.repository.designer.PortfolioRepository;
import com.jjs.ClothingInventorySaleReformPlatform.service.s3.S3Uploader;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PortfolioService {

    private final PortfolioRepository portfolioRepository;
    private final S3Uploader s3Uploader;

    /**
     *
     * portfolio 저장 메소드
     * @param portfolioDTO(포트폴리오 입력 정보)
     * @throws IOException
     */

    public void savePortfolio(PortfolioDTO portfolioDTO) throws IOException {
        Portfolio portfolio = new Portfolio();
        User user = new User();  //이메일은 User타입이기 때문에 엔티티로 변환해주는 과정 필요

        user.setEmail(portfolioDTO.getDesignerEmail());
        Optional<Portfolio> storedDesignerEmail = portfolioRepository.findByDesignerEmail(user);

        // 이메일 이미 존재 할 경우
        if (storedDesignerEmail.isPresent()) {
            throw new RuntimeException("이미 존재하는 이메일입니다.");
        }
        else{
            user.setEmail(portfolioDTO.getDesignerEmail());
            portfolio.setDesignerEmail(user);
            portfolio.setExplanation(portfolioDTO.getExplanation());
            portfolio.setDesignerImage(s3Uploader.uploadFile(portfolioDTO.getDesignerImage()));

            portfolioRepository.save(portfolio);
        }

    }

    /**
     * Repository에서 포트폴리오 정보 조회 메소드
     * @param designerEmail -> String으로 입력 받으나, 엔티티 타입으로 선언되어 있기에 User 타입으로 변환하여 사용해야 함.
     * @return
     */
    public Optional<PortfolioInfoDTO> getPortfolio(String designerEmail){

        User user = new User();
        user.setEmail(designerEmail);

        Optional<Portfolio> portfolioByDesignerEmail = portfolioRepository.findByDesignerEmail(user);

        if(portfolioByDesignerEmail.isPresent()){
            Portfolio portfolio = portfolioByDesignerEmail.get(); //Optional 객체에서 엔티티 조회 값 추출
            PortfolioInfoDTO portfolioInfoDTO = new PortfolioInfoDTO();
            portfolioInfoDTO.setDesignerImagePath(portfolio.getDesignerImage());
            portfolioInfoDTO.setExplanation(portfolio.getExplanation());

            return Optional.of(portfolioInfoDTO);

        }else{
            throw new RuntimeException("포트폴리오가 존재하지 않습니다.");
        }

    }







}
