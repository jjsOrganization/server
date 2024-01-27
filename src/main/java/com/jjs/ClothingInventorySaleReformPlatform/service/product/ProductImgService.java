package com.jjs.ClothingInventorySaleReformPlatform.service.product;

import com.jjs.ClothingInventorySaleReformPlatform.domain.product.ProductImg;
import com.jjs.ClothingInventorySaleReformPlatform.repository.product.ProductImgRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductImgService {

    @Value("${itemImgLocation}")
    private String productImgLocation;

    private final ProductImgRepository productImgRepository;
    private final FileService fileService;

    public void saveItemImg(ProductImg productImg, MultipartFile productImgFile) throws Exception{
        String oriImgName = productImgFile.getOriginalFilename();  // 원래 파일명
        String imgName = "";
        String imgUrl = "";

        //파일 업로드
        if(!StringUtils.isEmpty(oriImgName)){  // db에 저장되는 가짜? 경로 느낌...
            imgName = fileService.uploadFile(productImgLocation, oriImgName, productImgFile.getBytes());
            imgUrl = "/images/product/" + imgName;
        }

        //상품 이미지 정보 저장
        productImg.updateItemImg(oriImgName, imgName, imgUrl);
        productImgRepository.save(productImg);
    }
}
