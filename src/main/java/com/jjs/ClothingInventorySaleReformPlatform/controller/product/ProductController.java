package com.jjs.ClothingInventorySaleReformPlatform.controller.product;

import com.jjs.ClothingInventorySaleReformPlatform.dto.product.ProductFormDTO;
import com.jjs.ClothingInventorySaleReformPlatform.dto.product.response.ProductDetailDTO;
import com.jjs.ClothingInventorySaleReformPlatform.dto.product.response.ProductListDTO;
import com.jjs.ClothingInventorySaleReformPlatform.error.ErrorCode;
import com.jjs.ClothingInventorySaleReformPlatform.error.ErrorResponse;
import com.jjs.ClothingInventorySaleReformPlatform.repository.product.ProductRepository;
import com.jjs.ClothingInventorySaleReformPlatform.response.AuthResultCode;
import com.jjs.ClothingInventorySaleReformPlatform.response.ResultResponse;
import com.jjs.ClothingInventorySaleReformPlatform.service.product.ProductImgService;
import com.jjs.ClothingInventorySaleReformPlatform.service.product.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@Slf4j
@Tag(name = "상품", description = "상품 관리와 관련된 API 입니다.")
public class ProductController {
    private final ProductRepository productRepository;

    private final ProductService productService;
    private final ProductImgService productImgService;


    private final AuthenticationFacade authenticationFacade;

    // 상품 등록
    @Operation(summary = "상품 등록", description = "상품 정보를 등록하는 것으로 이미지는 한 개 이상이 필수입니다.")
    @PostMapping("/product/seller/register")
    public ResponseEntity<Object> itemNew(@Valid @ModelAttribute ProductFormDTO productFormDTO,
                                           BindingResult bindingResult,
                                           @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList) {

        if (bindingResult.hasErrors()) {
            List<ErrorResponse.FieldError> fieldErrors = bindingResult.getFieldErrors().stream()
                    .map(fieldError -> new ErrorResponse.FieldError(
                            fieldError.getField(),
                            fieldError.getRejectedValue() == null ? "" : fieldError.getRejectedValue().toString(),
                            fieldError.getDefaultMessage()))
                    .collect(Collectors.toList());

            ErrorResponse errorResponse = new ErrorResponse(ErrorCode.INVALID_BAD_REQUEST, fieldErrors);
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        if (itemImgFileList.get(0).isEmpty() && productFormDTO.getId() == null) {
            List<ErrorResponse.FieldError> fieldErrors = bindingResult.getFieldErrors().stream()
                    .map(fieldError -> new ErrorResponse.FieldError(
                            fieldError.getField(),
                            fieldError.getRejectedValue() == null ? "" : fieldError.getRejectedValue().toString(),
                            fieldError.getDefaultMessage()))
                    .collect(Collectors.toList());

            ErrorResponse errorResponse = new ErrorResponse(ErrorCode.IMAGE_EMPTY, fieldErrors);
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        try {
            productService.saveItem(productFormDTO, itemImgFileList);
            ResultResponse resultResponse = ResultResponse.of(AuthResultCode.REGISTER_PRODUCT_SUCCESS, Collections.singletonMap("productName", productFormDTO.getProductName()));
            return new ResponseEntity<>(resultResponse, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("상품 등록 중 에러가 발생하였습니다.");
        }
    }

    // 상품 삭제
    @Operation(summary = "상품 삭제", description = "로그인한 판매자는 본인이 등록한 상품을 삭제한다.")
    @DeleteMapping("/product/seller/register/{productId}")  // (DELETE)http://localhost:8080/item/delete/1
    public ResponseEntity<?> deleteProduct(@PathVariable Long productId) {
        try {
            productService.deleteProduct(productId);
            return ResponseEntity.ok().body("Product deleted successfully");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error occurred during product deletion");
        }
    }

    // 로그인한 판매자의 상품 전체 조회  (GET)http://localhost:8080/item/register
    @Operation(summary = "상품 전체 조회(로그인한 판매자)", description = "로그인 한 판매자는 마이페이지 또는 재고목록 확인을 위해 자신이 등록한 상품을 모두 조회한다.(이미지 제외)")
    @GetMapping("/product/seller/register")
    public ResponseEntity<List<ProductListDTO>> getMyProducts() {
        String currentUsername = authenticationFacade.getCurrentUsername();
        List<ProductListDTO> products = productService.getProductsFindAll(currentUsername);
        return ResponseEntity.ok(products);
    }

    // 로그인한 판매자의 특정 상품 조회
    @Operation(summary = "상품 상세 조회(로그인한 판매자)", description = "로그인 한 판매자는 자신이 등록한 상품들 중 하나에 대하여 상세히 조회한다.(이미지 포함)")
    @GetMapping("/product/register/{productId}")
    public ResponseEntity<ProductDetailDTO> getProductDetail(@PathVariable Long productId) {
        String currentUsername = authenticationFacade.getCurrentUsername();
        return productService.getProductsFindOne(productId, currentUsername)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 판매자들이 등록한 전체 상품 조회
    @Operation(summary = "상품 전체 조회", description = "모든 회원은 판매자들이 등록한 상품들을 모두 조회할 수 있다.")
    @GetMapping("/product/all")
    public ResponseEntity<List<ProductListDTO>> getAllProducts() {
        List<ProductListDTO> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    // 상품 상세 조회
    @Operation(summary = "상품 상세 조회", description = "모든 회원은 판매자들이 등록한 상품들 중 하나에 대하여 상세히 조회할 수 있다.")
    @GetMapping("/product/all/detail/{productId}")
    public ResponseEntity<ProductDetailDTO> getProductDetails(@PathVariable Long productId) {
        return productService.getProductsFindDetail(productId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 특정 검색어가 포함된 상품들 전체 조회 - 상품 검색
    @Operation(summary = "상품 검색", description = "상품 전체 조회에 대하여 특정 검색어가 포함된 상품이 조회된다.")
    @GetMapping("/product/all/{keyword}")
    public ResponseEntity<List<ProductListDTO>> searchProductsByName(@PathVariable String keyword) {
        List<ProductListDTO> products = productService.searchProductsByName(keyword);
        return ResponseEntity.ok(products);
    }

    // 상품 수정
    @Operation(summary = "상품 수정", description = "로그인한 판매자는 자신이 등록한 상품에 대하여 수정이 가능하다.")
    @PutMapping("/product/seller/register/{productId}")
    public ResponseEntity<?> updateProduct(@PathVariable Long productId,
                                           @Valid @ModelAttribute ProductFormDTO productFormDTO,
                                           BindingResult bindingResult,
                                           @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList) {

        if (bindingResult.hasErrors()) {
            List<ErrorResponse.FieldError> fieldErrors = bindingResult.getFieldErrors().stream()
                    .map(fieldError -> new ErrorResponse.FieldError(
                            fieldError.getField(),
                            fieldError.getRejectedValue() == null ? "" : fieldError.getRejectedValue().toString(),
                            fieldError.getDefaultMessage()))
                    .collect(Collectors.toList());

            ErrorResponse errorResponse = new ErrorResponse(ErrorCode.INVALID_BAD_REQUEST, fieldErrors);
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        if (itemImgFileList.get(0).isEmpty() && productFormDTO.getId() == null) {
            List<ErrorResponse.FieldError> fieldErrors = bindingResult.getFieldErrors().stream()
                    .map(fieldError -> new ErrorResponse.FieldError(
                            fieldError.getField(),
                            fieldError.getRejectedValue() == null ? "" : fieldError.getRejectedValue().toString(),
                            fieldError.getDefaultMessage()))
                    .collect(Collectors.toList());

            ErrorResponse errorResponse = new ErrorResponse(ErrorCode.IMAGE_EMPTY, fieldErrors);
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        try {
            productService.updateProduct(productId, productFormDTO, itemImgFileList);
            ResultResponse resultResponse = ResultResponse.of(AuthResultCode.REGISTER_PRODUCT_SUCCESS, Collections.singletonMap("productName", productFormDTO.getProductName()));
            return new ResponseEntity<>(resultResponse, HttpStatus.OK);
        } catch (Exception e) {
            log.error("상품 수정 중 에러 발생", e);
            return ResponseEntity.internalServerError().body("상품 수정 중 에러가 발생하였습니다. 오류: " + e.getMessage());
        }
    }


    @Operation(summary = "카테고리별 상품 조회", description = "사용자가 선택한 카테고리에 속하는 상품들을 조회할 수 있다.")
    @GetMapping("/product/category/{categoryId}")
    public ResponseEntity<List<ProductListDTO>> getProductsByCategory(@PathVariable Long categoryId) {
        List<ProductListDTO> products = productService.getProductsByCategory(categoryId);
        return ResponseEntity.ok(products);
    }






}
