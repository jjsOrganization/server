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
public class ProductController {
    private final ProductRepository productRepository;

    private final ProductService productService;
    private final ProductImgService productImgService;


    private final AuthenticationFacade authenticationFacade;

    // 상품 등록
    @PostMapping("/item/register/new")
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
            //return ResponseEntity.badRequest().body("유효성 검사 실패");
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
            //return ResponseEntity.badRequest().body("첫번째 상품 이미지는 필수 입력 값 입니다.");
        }

        try {
            productService.saveItem(productFormDTO, itemImgFileList);
            ResultResponse resultResponse = ResultResponse.of(AuthResultCode.REGISTER_PRODUCT_SUCCESS, Collections.singletonMap("productName", productFormDTO.getProductName()));
            return new ResponseEntity<>(resultResponse, HttpStatus.OK);
            //return ResponseEntity.ok().body("상품이 성공적으로 등록되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("상품 등록 중 에러가 발생하였습니다.");
        }
    }

    // 상품 삭제
    @DeleteMapping("/item/delete/{productId}")  // (DELETE)http://localhost:8080/item/delete/1
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
    @GetMapping("/item/register")
    public ResponseEntity<List<ProductListDTO>> getMyProducts() {
        String currentUsername = authenticationFacade.getCurrentUsername();
        List<ProductListDTO> products = productService.getProductsFindAll(currentUsername);
        return ResponseEntity.ok(products);
    }

    // 로그인한 판매자의 특정 상품 조회
    @GetMapping("/item/register/{productId}")
    public ResponseEntity<ProductDetailDTO> getProductDetail(@PathVariable Long productId) {
        String currentUsername = authenticationFacade.getCurrentUsername();
        return productService.getProductsFindOne(productId, currentUsername)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 판매자들이 등록한 전체 상품 조회
    @GetMapping("/item/all")
    public ResponseEntity<List<ProductListDTO>> getAllProducts() {
        List<ProductListDTO> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    // 특정 검색어가 포함된 상품들 전체 조회 - 상품 검색
    @GetMapping("/all/{keyword}")
    public ResponseEntity<List<ProductListDTO>> searchProductsByName(@PathVariable String keyword) {
        List<ProductListDTO> products = productService.searchProductsByName(keyword);
        return ResponseEntity.ok(products);
    }





    // 상품 수정
    @PutMapping("/item/register/{productId}")
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
            //return ResponseEntity.badRequest().body("유효성 검사 실패");
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
            //return ResponseEntity.badRequest().body("첫번째 상품 이미지는 필수 입력 값 입니다.");
        }

        try {
            //productService.updateProduct(productId, productFormDTO, itemImgFileList);
            productService.updateProduct(productId, productFormDTO, itemImgFileList);
            //productService.updateProduct(productFormDTO.getId(), productFormDTO, itemImgFileList);
            ResultResponse resultResponse = ResultResponse.of(AuthResultCode.REGISTER_PRODUCT_SUCCESS, Collections.singletonMap("productName", productFormDTO.getProductName()));
            return new ResponseEntity<>(resultResponse, HttpStatus.OK);
            //return ResponseEntity.ok().body("상품이 성공적으로 등록되었습니다.");
        } catch (Exception e) {
            //return ResponseEntity.internalServerError().body("상품 등록 중 에러가 발생하였습니다.");
            log.error("상품 수정 중 에러 발생", e);
            return ResponseEntity.internalServerError().body("상품 수정 중 에러가 발생하였습니다. 오류: " + e.getMessage());
        }
    }






}
