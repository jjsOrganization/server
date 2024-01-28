package com.jjs.ClothingInventorySaleReformPlatform.service.product;

import com.jjs.ClothingInventorySaleReformPlatform.domain.product.ProductImg;
import com.jjs.ClothingInventorySaleReformPlatform.domain.product.Product;
import com.jjs.ClothingInventorySaleReformPlatform.domain.product.ProductSellStatus;
import com.jjs.ClothingInventorySaleReformPlatform.dto.product.ProductFormDTO;
import com.jjs.ClothingInventorySaleReformPlatform.dto.product.response.ProductDetailDTO;
import com.jjs.ClothingInventorySaleReformPlatform.dto.product.response.ProductListDTO;
import com.jjs.ClothingInventorySaleReformPlatform.repository.product.ProductImgRepository;
import com.jjs.ClothingInventorySaleReformPlatform.repository.product.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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


    // 상품 삭제
    public void deleteProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        // 상품 이미지 삭제
        product.getProductImg().forEach(productImg -> {
            productImgService.deleteProductImg(productImg.getId());
        });

        // 상품 정보 삭제
        productRepository.delete(product);
    }


    // 상품 수정
    public Long updateProduct(Long productId, ProductFormDTO productFormDTO, List<MultipartFile> productImgFileList) throws Exception {
        // 상품 정보 조회 및 업데이트
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        product.updateProduct(productFormDTO); // 상품 정보 업데이트

        // 이미지 파일 리스트가 비어있지 않고, 제공된 이미지가 있는 경우에만 이미지 처리
        if (!productImgFileList.isEmpty()) {
            List<Long> productImgIds = productFormDTO.getProductImgIds();

            for (int i = 0; i < productImgFileList.size(); i++) {
                MultipartFile file = productImgFileList.get(i);

                if (!file.isEmpty()) {
                    if (i < productImgIds.size()) {
                        // 기존 이미지 업데이트
                        productImgService.updateProductImg(productImgIds.get(i), file);
                    } else {
                        // 새 이미지 추가
                        productImgService.uploadFile(new ProductImg(), file);
                    }
                }
            }
        }

        return product.getId();
    }

    // 로그인한 판매자가 등록한 전체 상품 조회
    public List<ProductListDTO> getProductsFindAll(String createBy) {
        return productRepository.findByCreateBy(createBy)
                .stream()
                .map(this::productsFindAll)
                .collect(Collectors.toList());
    }
    private ProductListDTO productsFindAll(Product product) {  // 상품 전체 조회 dto
        ProductListDTO dto = new ProductListDTO();
        dto.setId(product.getId());
        dto.setProductName(product.getProductName());
        dto.setPrice(product.getPrice());
        dto.setItemDetail(product.getProductDetailText());
        dto.setProductStock(product.getProductStock());
        dto.setProductSellStatus(product.getProductSellStatus());
        return dto;
    }

    // 로그인한 판매자가 등록한 상품 중 하나 상세 조회
    public Optional<ProductDetailDTO> getProductsFindOne(Long productId, String sellerUsername) {
        return productRepository.findById(productId)
                .filter(product -> product.getCreateBy().equals(sellerUsername))
                .map(this::productsFindOne);
    }
    private ProductDetailDTO productsFindOne(Product product) {  // 상품 상세 조회 dto
        ProductDetailDTO dto = new ProductDetailDTO();
        dto.setId(product.getId());
        dto.setProductName(product.getProductName());
        dto.setPrice(product.getPrice());
        dto.setItemDetail(product.getProductDetailText());
        dto.setProductStock(product.getProductStock());
        dto.setProductSellStatus(product.getProductSellStatus());
        dto.setProductImg(product.getProductImg());
        return dto;
    }

    // 판매자들이 등록한 상품들 전체 조회(모든 상품)
    public List<ProductListDTO> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(this::productsFindAll)
                .collect(Collectors.toList());
    }

    // 특정 검색어가 포함된 상품들 전체 조회
    public List<ProductListDTO> searchProductsByName(String keyword) {
        return productRepository.findByProductNameContaining(keyword)
                .stream()
                .map(this::productsFindAll)
                .collect(Collectors.toList());
    }
}
