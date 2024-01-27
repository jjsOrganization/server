package com.jjs.ClothingInventorySaleReformPlatform.service.product;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.jjs.ClothingInventorySaleReformPlatform.domain.product.ProductImg;
import com.jjs.ClothingInventorySaleReformPlatform.repository.product.ProductImgRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductImgService {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private String defaultUrl = "https://s3.amazonaws.com/";

    private final ProductImgRepository productImgRepository;

    public String uploadFile(ProductImg productImg, MultipartFile file) throws Exception {
        String oriImgName = file.getOriginalFilename();
        String fileName = generateFileName(file);
        String filePath = "productRegister/" + fileName;

        try {
            amazonS3Client.putObject(bucket, filePath, file.getInputStream(), getObjectMetadata(file));

            productImg.updateItemImg(oriImgName, fileName, filePath);
            productImgRepository.save(productImg);
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
     * 고유한 파일명을 생성
     *
     * @param file 업로드된 파일
     * @return 고유한 파일명
     */
    private String generateFileName(MultipartFile file) {
        return UUID.randomUUID().toString() + "-" + file.getOriginalFilename();
    }

}

