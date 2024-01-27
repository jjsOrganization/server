package com.jjs.ClothingInventorySaleReformPlatform.controller.product;

import com.jjs.ClothingInventorySaleReformPlatform.dto.product.ProductFormDTO;
import com.jjs.ClothingInventorySaleReformPlatform.error.ErrorCode;
import com.jjs.ClothingInventorySaleReformPlatform.error.ErrorResponse;
import com.jjs.ClothingInventorySaleReformPlatform.response.AuthResultCode;
import com.jjs.ClothingInventorySaleReformPlatform.response.ResultResponse;
import com.jjs.ClothingInventorySaleReformPlatform.service.product.ProductImgService;
import com.jjs.ClothingInventorySaleReformPlatform.service.product.ProductService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
public class ProductController {

    private final ProductService productService;
    private final ProductImgService productImgService;


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


}
