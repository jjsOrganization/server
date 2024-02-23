package com.jjs.ClothingInventorySaleReformPlatform.controller.reformrequest;

import com.jjs.ClothingInventorySaleReformPlatform.dto.product.response.ProductDetailDTO;
import com.jjs.ClothingInventorySaleReformPlatform.dto.reformrequest.ReformProductInfoDTO;
import com.jjs.ClothingInventorySaleReformPlatform.dto.reformrequest.ReformRequestDTO;
import com.jjs.ClothingInventorySaleReformPlatform.dto.response.Response;
import com.jjs.ClothingInventorySaleReformPlatform.error.ErrorCode;
import com.jjs.ClothingInventorySaleReformPlatform.error.ErrorResponse;
import com.jjs.ClothingInventorySaleReformPlatform.service.reformrequest.ReformRequestService;
import com.jjs.ClothingInventorySaleReformPlatform.service.s3.S3Service;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.http.protocol.HTTP;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@Getter
@Setter
@RequiredArgsConstructor
@Tag(name = "리폼 의뢰 신청", description = "구매자가 디자이너에게 리폼 신청하는 API 입니다.")
public class ReformRequestController {
    private final ReformRequestService reformRequestService;
    private final Response response;
    @GetMapping("/reform-request/purchaser/{itemId}")
    @Operation(summary = "리폼 의뢰 페이지 이동", description = "리폼 의뢰를 위한 폼을 불러옵니다.")
    public ResponseEntity<?> getReformRequestForm(@Valid @PathVariable Long itemId, BindingResult bindingResult) { 
        ResponseEntity<?> errorResponse = getObjectResponseEntity(bindingResult.hasErrors(),
                bindingResult, ErrorCode.INVALID_BAD_REQUEST);
        if (errorResponse != null) return errorResponse;

        try{
            ReformProductInfoDTO reformProductInfoDTO = reformRequestService.getProductInfo(itemId);
            return response.success(reformProductInfoDTO, "리폼 페이지에 접속했습니다.", HttpStatus.OK);

        }catch(Exception e){
            return response.fail("상품 정보를 찾을 수 없습니다.", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/reform-request/purchaser/{itemId}")
    public ResponseEntity<?> sendReformRequest(@ModelAttribute ReformRequestDTO reformRequestDTO,  BindingResult bindingResult,
                                               @PathVariable Long itemId) throws Exception {
        reformRequestService.saveReformRequest(reformRequestDTO, itemId);

        return response.success("의뢰 성공.", HttpStatus.OK);
    }

    private String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getName();
        }
        return null;
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
