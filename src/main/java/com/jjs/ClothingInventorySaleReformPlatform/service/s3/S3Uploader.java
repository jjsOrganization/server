package com.jjs.ClothingInventorySaleReformPlatform.service.s3;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class S3Uploader {
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    private final AmazonS3Client amazonS3Client;

    private String defaultUrl = "https://s3.amazonaws.com/";

    /**
     * S3에 이미지를 업로드
     *
     * @param file 업로드할 이미지 파일
     * @return S3에 저장된 이미지의 URL
     * @throws IOException 파일 업로드 중 발생한 예외
     */

    @Transactional
    public String uploadFile(MultipartFile file) throws IOException {
        // 저장될 파일의 경로 설정
        String storedFileName = generateFileName(file);

        try {
            // S3에 파일 업로드
            amazonS3Client.putObject(bucket, "PortfolioImages/" + storedFileName, file.getInputStream(), getObjectMetadata(file));

            System.out.println(amazonS3Client.getUrl(bucket, generateFileName(file)).toString());
            // 업로드된 이미지의 URL 반환
            return amazonS3Client.getUrl(bucket, generateFileName(file)).toString();

        } catch (SdkClientException e) {
            throw new IOException("Error uploading file to S3", e);
        }
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
     * 파일의 확장자 추출
     */

    public String extractExtension(String originName) {
        int index = originName.lastIndexOf('.');

        return originName.substring(index, originName.length());
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
