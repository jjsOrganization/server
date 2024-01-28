package com.jjs.ClothingInventorySaleReformPlatform.controller.product;

import com.jjs.ClothingInventorySaleReformPlatform.dto.product.ProductFormDTO;
import com.jjs.ClothingInventorySaleReformPlatform.error.ErrorCode;
import com.jjs.ClothingInventorySaleReformPlatform.error.ErrorResponse;
import com.jjs.ClothingInventorySaleReformPlatform.response.AuthResultCode;
import com.jjs.ClothingInventorySaleReformPlatform.response.ResultResponse;
import com.jjs.ClothingInventorySaleReformPlatform.service.product.ProductImgService;
import com.jjs.ClothingInventorySaleReformPlatform.service.product.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final ProductImgService productImgService;


    @PostMapping("/item/new")
    public ResponseEntity<Object> itemNew(@Valid @ModelAttribute ProductFormDTO productFormDTO,
                                           BindingResult bindingResult,
                                           @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList) {

        ResponseEntity<Object> errorResponse = getObjectResponseEntity(bindingResult.hasErrors(), bindingResult, ErrorCode.INVALID_BAD_REQUEST);
        if (errorResponse != null) return errorResponse;
        //return ResponseEntity.badRequest().body("유효성 검사 실패");

        ResponseEntity<Object> errorResponse1 = getObjectResponseEntity(itemImgFileList.get(0).isEmpty() && productFormDTO.getId() == null,
                bindingResult, ErrorCode.IMAGE_EMPTY);
        if (errorResponse1 != null) return errorResponse1;

        try {
            productService.saveItem(productFormDTO, itemImgFileList);
            ResultResponse resultResponse = ResultResponse.of(AuthResultCode.REGISTER_PRODUCT_SUCCESS,
                    Collections.singletonMap("productName", productFormDTO.getProductName()));
            return new ResponseEntity<>(resultResponse, HttpStatus.OK);
            //return ResponseEntity.ok().body("상품이 성공적으로 등록되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("상품 등록 중 에러가 발생하였습니다.");
        }
    }

    private static ResponseEntity<Object> getObjectResponseEntity(boolean bindingResult, BindingResult bindingResult1, ErrorCode invalidBadRequest) {
        if (bindingResult) {
            List<ErrorResponse.FieldError> fieldErrors = bindingResult1.getFieldErrors().stream()
                    .map(fieldError -> new ErrorResponse.FieldError(
                            fieldError.getField(),
                            fieldError.getRejectedValue() == null ? "" : fieldError.getRejectedValue().toString(),
                            fieldError.getDefaultMessage()))
                    .collect(Collectors.toList());

            ErrorResponse errorResponse = new ErrorResponse(invalidBadRequest, fieldErrors);
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
        return null;
    }


}
