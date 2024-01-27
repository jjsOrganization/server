package com.jjs.ClothingInventorySaleReformPlatform.service.product;

import com.jjs.ClothingInventorySaleReformPlatform.domain.SellerInfo;
import com.jjs.ClothingInventorySaleReformPlatform.domain.product.ProductImg;
import com.jjs.ClothingInventorySaleReformPlatform.domain.product.Product;
import com.jjs.ClothingInventorySaleReformPlatform.domain.product.ProductSellStatus;
import com.jjs.ClothingInventorySaleReformPlatform.dto.product.ProductFormDTO;
import com.jjs.ClothingInventorySaleReformPlatform.repository.auth.SellerRepository;
import com.jjs.ClothingInventorySaleReformPlatform.repository.product.ProductImgRepository;
import com.jjs.ClothingInventorySaleReformPlatform.repository.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductImgService productImgService;
    private final ProductImgRepository productImgRepository;



    public Long saveItem(ProductFormDTO productFormDTO, List<MultipartFile> itemImgFileList) throws Exception{

        //상품 등록
        Product product = new Product();
        product.setProductName(productFormDTO.getProductName());
        product.setPrice(productFormDTO.getPrice());
        product.setProductStock(productFormDTO.getProductStock());
        product.setProductDetailText(productFormDTO.getItemDetail());
        product.setProductSellStatus(ProductSellStatus.SELL);  // 초기값은 판매중으로 고정
        productRepository.save(product);

        //이미지 등록
        for(int i=0; i<itemImgFileList.size(); i++){
            ProductImg productImg = new ProductImg();
            productImg.setProduct(product);

            if(i == 0)
                productImg.setRepimgYn("Y");
            else
                productImg.setRepimgYn("N");

            productImgService.uploadFile(productImg, itemImgFileList.get(i));
        }

        return product.getId();

    }
}
