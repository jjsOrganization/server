package com.jjs.ClothingInventorySaleReformPlatform.service.designer;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.jjs.ClothingInventorySaleReformPlatform.domain.Portfolio;
import com.jjs.ClothingInventorySaleReformPlatform.domain.User;
import com.jjs.ClothingInventorySaleReformPlatform.dto.designer.PortfolioDTO;
import com.jjs.ClothingInventorySaleReformPlatform.repository.designer.PortfolioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j

public class PortfolioService {

    private final PortfolioRepository portfolioRepository;
    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private String defaultUrl = "https://s3.amazonaws.com/";

    /**
     * S3에 이미지를 업로드
     *
     * @param file 업로드할 이미지 파일
     * @return S3에 저장된 이미지의 URL
     * @throws IOException 파일 업로드 중 발생한 예외
     */
    public String uploadFile(MultipartFile file) throws IOException {
        // 저장될 파일의 경로 설정
        String filePath = "images/" + generateFileName(file);

        try {
            // S3에 파일 업로드
            amazonS3Client.putObject(bucket, filePath, file.getInputStream(), getObjectMetadata(file));

            // 업로드된 이미지의 URL 반환
            return amazonS3Client.getUrl(bucket, generateFileName(file)).toString();
        } catch (SdkClientException e) {
            throw new IOException("Error uploading file to S3", e);
        }
    }

    /**
     *
     * portfolio 저장 메소드
     * @param portfolioDTO(포트폴리오 입력 정보)
     * @throws IOException
     */
    public void savePortfolio(PortfolioDTO portfolioDTO) throws IOException {

        String designerEmail = portfolioDTO.getDesignerEmail();
        String explanation = portfolioDTO.getExplanation();
        String preResultsExplanation = portfolioDTO.getPreResultsExplanation();
        String preResultsImageUrl = uploadFile(portfolioDTO.getPreResultsImage());

        /**
         * email당 포트폴리오 정보가 1개씩인지?? 아니라면 냅두고 그렇다면 중복검사해서 update 필요
         */

        Portfolio portfolio = new Portfolio();
        //이메일은 User타입이기 때문에 엔티티로 변환해주는 과정 필요
        User user = new User();
        user.setEmail(designerEmail);
        portfolio.setDesingerEmail(user);

        portfolio.setExplanation(explanation);
        portfolio.setPreviousResultsImgUrl(preResultsImageUrl);
        portfolio.setPreviousResultsExplanation(preResultsExplanation);

        portfolioRepository.save(portfolio);
    }


    /**
     * 업로드된 파일의 메타데이터를 생성
     *
     * @param file 업로드된 파일
     * @return 파일의 메타데이터
     */
    private ObjectMetadata getObjectMetadata(MultipartFile file) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentDisposition(file.getContentType());
        objectMetadata.setContentLength(file.getSize());
        return objectMetadata;
    }

    /**
     * 고유한 파일명을 생성
     *
     * @param file 업로드된 파일
     * @return 고유한 파일명
     */
    private String generateFileName(MultipartFile file) {
        return UUID.randomUUID().toString() + "-" + file.getOriginalFilename();
    }




}
