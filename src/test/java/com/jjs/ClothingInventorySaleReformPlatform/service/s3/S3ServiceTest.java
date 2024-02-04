package com.jjs.ClothingInventorySaleReformPlatform.service.s3;

import com.jjs.ClothingInventorySaleReformPlatform.repository.designer.PortfolioRepository;
import com.jjs.ClothingInventorySaleReformPlatform.repository.designer.mapping.ImageUrlMapping;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.UnsupportedEncodingException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@SpringBootTest
class S3ServiceTest {

    @Autowired
    S3Service s3Service;

    @Autowired
    PortfolioRepository portfolioRepository;

    @Test
    void getFileKeyFromUrl() throws UnsupportedEncodingException {
        Optional<ImageUrlMapping> portfolioById = portfolioRepository.findPortfolioById(8L);
        ImageUrlMapping img = portfolioById.get();
        String imageUrlMapping = img.getDesignerImage();


        String fileKeyFromUrl = s3Service.getFileKeyFromUrl(imageUrlMapping);

        assertThat(fileKeyFromUrl).isEqualTo("PortfolioImages/6afb12bc-bbec-4f23-bb26-0fe4f044fdd1-2024-01-25 213109.png");

    }
}