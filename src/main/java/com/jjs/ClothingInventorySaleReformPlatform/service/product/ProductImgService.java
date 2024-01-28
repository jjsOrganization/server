package com.jjs.ClothingInventorySaleReformPlatform.service.product;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.util.StringUtils;
import com.jjs.ClothingInventorySaleReformPlatform.domain.product.Product;
import com.jjs.ClothingInventorySaleReformPlatform.domain.product.ProductImg;
import com.jjs.ClothingInventorySaleReformPlatform.repository.product.ProductImgRepository;
import com.jjs.ClothingInventorySaleReformPlatform.repository.product.ProductRepository;
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

    // 상품 삭제에서 사용
    public void deleteProductImg(Long productImgId) {
        ProductImg productImg = productImgRepository.findById(productImgId)
                .orElseThrow(() -> new EntityNotFoundException("Image not found"));

        // S3에서 이미지 파일 삭제
        if (productImg.getImgName() != null && !productImg.getImgName().isEmpty()) {
            String filePath = "productRegister/" + productImg.getImgName();
            amazonS3Client.deleteObject(bucket, filePath);
        }

        // 데이터베이스에서 ProductImg 레코드 삭제
        productImgRepository.delete(productImg);
    }

    public void updateProductImg(Long productId, MultipartFile file) throws IOException {
        if (!file.isEmpty()) {
            /*
            ProductImg savedProductImg = productImgRepository.findById(productImgId)
                    .orElseThrow(EntityNotFoundException::new); */
            /*
            ProductImg savedProductImg = productImgRepository.findById(productImgId)
                    .orElseThrow(() -> new EntityNotFoundException("Image not found"));
            System.out.println(savedProductImg.getImgName());
            // S3에서 기존 이미지 파일 삭제
            if (!StringUtils.isNullOrEmpty(savedProductImg.getImgName())) {
                String oldFilePath = "productRegister/" + savedProductImg.getImgName();
                amazonS3Client.deleteObject(bucket, oldFilePath);
                //productImgRepository.delete(savedProductImg); // DB에서 삭제
            }

             */
            // 특정 상품에 연결된 이미지를 찾습니다.
            List<ProductImg> productImgs = productImgRepository.findByProductId(productId);

            // 이미지가 이미 존재하는 경우, 기존 이미지를 업데이트합니다.
            if (!productImgs.isEmpty()) {
                for (ProductImg productImg : productImgs) {
                    // S3에서 기존 이미지 파일 삭제
                    if (!StringUtils.isNullOrEmpty(productImg.getImgName())) {
                        String oldFilePath = "productRegister/" + productImg.getImgName();
                        amazonS3Client.deleteObject(bucket, oldFilePath);
                    }
                    // 데이터베이스에서 ProductImg 레코드 삭제
                    productImgRepository.delete(productImg);
                }
            }


            // S3에 새 이미지 파일 업로드
            String oriImgName = file.getOriginalFilename();
            String imgName = generateFileName(file);
            String imgUrl = "productRegister/" + imgName;

/*
            // 상품 이미지 정보 업데이트
            savedProductImg.updateItemImg(oriImgName, imgName, imgUrl);
            productImgRepository.save(savedProductImg);
 */

            ProductImg newProductImg = new ProductImg();
            newProductImg.setProduct((Product) productImgRepository.findByProductId(productId)); ///
            newProductImg.setImgName(imgName);
            newProductImg.setOriImgName(oriImgName);
            newProductImg.setImgUrl(imgUrl);
            newProductImg.setProduct(productRepository.findById(productId)
                    .orElseThrow(() -> new EntityNotFoundException("Product not found")));

            productImgRepository.save(newProductImg); // 새 이미지 정보 저장
        }
        // 이미지가 비어 있으면 이미지 업데이트를 하지 않음
    }


}

