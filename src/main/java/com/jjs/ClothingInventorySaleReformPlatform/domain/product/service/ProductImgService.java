package com.jjs.ClothingInventorySaleReformPlatform.domain.product.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.jjs.ClothingInventorySaleReformPlatform.domain.product.entity.ProductImg;
import com.jjs.ClothingInventorySaleReformPlatform.domain.product.repository.ProductImgRepository;
import com.jjs.ClothingInventorySaleReformPlatform.domain.product.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ProductImgService {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private String defaultUrl = "https://s3.amazonaws.com/";

    private final ProductImgRepository productImgRepository;
    private final ProductRepository productRepository;

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

    public void fileDelete(String fileName) {
        log.info("filename : " + fileName);
        try {
            amazonS3Client.deleteObject(this.bucket, fileName);
        } catch (AmazonServiceException e) {
            System.err.println(e.getErrorMessage());
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

    // 상품 이미지 삭제
    public void deleteImagesFromS3(List<ProductImg> productImgs) {
        productImgs.forEach(productImg -> {
            if (productImg.getImgName() != null && !productImg.getImgName().isEmpty()) {
                String filePath = "productRegister/" + productImg.getImgName();
                amazonS3Client.deleteObject(bucket, filePath);
            }
        });
    }




    public void updateProductImages(Long productId, List<MultipartFile> productImgFileList) throws IOException {
        List<ProductImg> existingImgs = productImgRepository.findByProductId(productId);

        // S3 및 데이터베이스에서 기존 이미지 삭제
        existingImgs.forEach(productImg -> {
            if (productImg.getImgName() != null && !productImg.getImgName().isEmpty()) {
                amazonS3Client.deleteObject(bucket, "productRegister/" + productImg.getImgName());
                productImgRepository.delete(productImg);
            }
        });

        // 새 이미지 파일 업로드 및 데이터베이스 저장
        for (MultipartFile file : productImgFileList) {
            if (!file.isEmpty()) {
                uploadNewProductImg(productId, file);
            }
        }
    }
    private void uploadNewProductImg(Long productId, MultipartFile file) throws IOException {
        String oriImgName = file.getOriginalFilename();
        String imgName = generateFileName(file);
        String imgUrl = "productRegister/" + imgName;

        // S3에 이미지 파일 업로드
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());
        amazonS3Client.putObject(new PutObjectRequest(bucket, imgUrl, file.getInputStream(), metadata));

        // 새 이미지 정보 생성 및 저장
        ProductImg newProductImg = new ProductImg();
        newProductImg.setProduct(productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found")));
        newProductImg.setImgName(imgName);
        newProductImg.setOriImgName(oriImgName);
        newProductImg.setImgUrl(imgUrl);

        productImgRepository.save(newProductImg);
    }

}

