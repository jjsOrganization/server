package com.jjs.ClothingInventorySaleReformPlatform.domain.product.service;

import com.jjs.ClothingInventorySaleReformPlatform.domain.product.entity.ProductImg;
import com.jjs.ClothingInventorySaleReformPlatform.domain.product.entity.Product;
import com.jjs.ClothingInventorySaleReformPlatform.domain.product.entity.ProductSellStatus;
import com.jjs.ClothingInventorySaleReformPlatform.domain.product.entity.ProductLikeCount;
import com.jjs.ClothingInventorySaleReformPlatform.domain.product.dto.request.ProductFormDTO;
import com.jjs.ClothingInventorySaleReformPlatform.domain.product.dto.response.ProductDetailDTO;
import com.jjs.ClothingInventorySaleReformPlatform.domain.product.dto.response.ProductListDTO;
import com.jjs.ClothingInventorySaleReformPlatform.domain.product.repository.ProductImgRepository;
import com.jjs.ClothingInventorySaleReformPlatform.domain.product.repository.ProductLikeCountRepository;
import com.jjs.ClothingInventorySaleReformPlatform.domain.product.repository.ProductRepository;
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
    private final ProductLikeCountRepository productLikeCountRepository;


    // 상품 등록
    public Long saveItem(ProductFormDTO productFormDTO, List<MultipartFile> itemImgFileList) throws Exception{

        //상품 등록
        Product product = new Product();
        product.setProductName(productFormDTO.getProductName());
        product.setPrice(productFormDTO.getPrice());
        product.setProductStock(productFormDTO.getProductStock());
        product.setProductDetailText(productFormDTO.getItemDetail());
        product.setProductSellStatus(ProductSellStatus.SELL);  // 초기값은 판매중으로 고정
        product.setCategory(productFormDTO.getCategoryId());
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

        // 상품 정보 삭제(삭제 여부를 true로 설정)
        product.setDeleted(true);

        // S3에서 이미지 파일 삭제 로직은 별도 메소드로 분리될 수 있음
        //productImgService.deleteImagesFromS3(product.getProductImg());
    }

    // 상품 수정 - 게시글 수정 시, 기존의 이미지를 삭제하고 새로운 이미지를 추가하는 방식
    @Transactional
    public Long updateProduct(Long productId, ProductFormDTO productFormDTO, List<MultipartFile> productImgFileList) throws Exception {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        product.updateProduct(productFormDTO); // 상품 정보 업데이트

        if (!productImgFileList.isEmpty()) {
            productImgService.updateProductImages(productId, productImgFileList);
        }

        return product.getId();
    }

    // 로그인한 판매자가 등록한 전체 상품 조회
    public List<ProductListDTO> getProductsFindAll(String createBy) {
        List<Product> products = productRepository.findByCreateByAndIsDeletedFalse(createBy);
        return products.stream().map(product -> {
            Long likeCount = productLikeCountRepository.findByProductId(product.getId())
                    .map(ProductLikeCount::getLikeCount).orElse(0L); // 상품 ID를 기준으로 좋아요 개수 조회
            return productsFindAll(product, likeCount); // 상품 정보와 좋아요 개수를 포함하여 DTO 변환
        }).collect(Collectors.toList());
    }
    private ProductListDTO productsFindAll(Product product, Long likeCount) {  // 상품 전체 조회 dto
        ProductListDTO dto = new ProductListDTO();
        dto.setId(product.getId());
        dto.setProductName(product.getProductName());
        dto.setPrice(product.getPrice());
        dto.setItemDetail(product.getProductDetailText());
        dto.setProductStock(product.getProductStock());
        dto.setProductSellStatus(product.getProductSellStatus());
        dto.setLikeCount(likeCount);
        // 카테고리 이름 설정
        if (product.getCategory() != null) {
            dto.setCategoryName(product.getCategory().getCategoryName());
        }

        // 상품 이미지 추가함...
        if (!product.getProductImg().isEmpty()) {
            dto.setImgUrl(product.getProductImg().get(0).getImgUrl());
        } else {
            dto.setImgUrl(null);
        }
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
        List<Product> products = productRepository.findByIsDeletedFalse();
        return products.stream().map(product -> {
            Long likeCount = productLikeCountRepository.findByProductId(product.getId())
                    .map(ProductLikeCount::getLikeCount).orElse(0L); // 없는 경우 0으로 처리
            return productsFindAll(product, likeCount); // 상품 정보와 좋아요 개수를 DTO로 변환
        }).collect(Collectors.toList());
    }

    // 특정 검색어가 포함된 상품들 전체 조회
    public List<ProductListDTO> searchProductsByName(String keyword) {
        List<Product> products = productRepository.findByProductNameContaining(keyword);
        return products.stream().map(product -> {
            Long likeCount = productLikeCountRepository.findByProductId(product.getId())
                    .map(ProductLikeCount::getLikeCount).orElse(0L); // 상품 ID를 기준으로 좋아요 개수 조회, 없으면 0
            return productsFindAll(product, likeCount); // 상품 정보와 좋아요 개수를 포함하여 DTO 변환
        }).collect(Collectors.toList());
        /*
        return productRepository.findByProductNameContaining(keyword)
                .stream()
                .map(this::productsFindAll)
                .collect(Collectors.toList());
         */
    }

    // 상품 상세 조회
    public Optional<ProductDetailDTO> getProductsFindDetail(Long productId) {
        return productRepository.findByIdAndIsDeletedFalse(productId)
                .map(this::productsFindOne);
    }

    // 카테고리별 상품 조회
    public List<ProductListDTO> getProductsByCategory(Long categoryId) {
        List<Product> products = productRepository.findByCategoryIdAndIsDeletedFalse(categoryId);
        return products.stream().map(product -> {
            Long likeCount = productLikeCountRepository.findByProductId(product.getId())
                    .map(ProductLikeCount::getLikeCount).orElse(0L); // 상품 ID를 기준으로 좋아요 개수 조회
            return productsFindAll(product, likeCount); // 상품 정보와 좋아요 개수를 포함하여 DTO 변환
        }).collect(Collectors.toList());
        /*
        return productRepository.findByCategoryId(categoryId)
                .stream()
                // 상품 엔티티를 ProductListDTO로 변환
                .map(this::productsFindAll)
                .collect(Collectors.toList());
         */

    }
}
