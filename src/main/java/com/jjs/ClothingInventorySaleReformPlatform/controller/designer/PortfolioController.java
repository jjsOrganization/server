package com.jjs.ClothingInventorySaleReformPlatform.controller.designer;

import com.jjs.ClothingInventorySaleReformPlatform.domain.User;
import com.jjs.ClothingInventorySaleReformPlatform.dto.designer.PortfolioDTO;
import com.jjs.ClothingInventorySaleReformPlatform.dto.designer.PortfolioInfoDTO;
import com.jjs.ClothingInventorySaleReformPlatform.error.ErrorCode;
import com.jjs.ClothingInventorySaleReformPlatform.error.ErrorResponse;
import com.jjs.ClothingInventorySaleReformPlatform.response.AuthResultCode;
import com.jjs.ClothingInventorySaleReformPlatform.response.ResultResponse;
import com.jjs.ClothingInventorySaleReformPlatform.service.designer.PortfolioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Tag(name = "포트폴리오 컨트롤러", description = "포트폴리오 API 입니다.")
public class PortfolioController {

    private final PortfolioService portfolioService;

    @Operation(summary = "포트폴리오 등록", description = "입력한 포트폴리오 정보를 저장합니다.")
    @PostMapping("/designer/portfolio")
    public ResponseEntity<Object> uploadPortfolio(@Valid @ModelAttribute PortfolioDTO portfolioDTO,
                                                  BindingResult bindingResult) throws IOException {

        ResponseEntity<Object> errorResponse = getObjectResponseEntity(bindingResult.hasErrors(),
                bindingResult, ErrorCode.INVALID_BAD_REQUEST);
        if (errorResponse != null) return errorResponse;

        ResponseEntity<Object> imageErrorResponse = getObjectResponseEntity(portfolioDTO.getDesignerImage().isEmpty(),
                bindingResult, ErrorCode.IMAGE_EMPTY);
        if (imageErrorResponse != null) return imageErrorResponse;

        try {
            portfolioService.savePortfolio(portfolioDTO);

            // 수정 필요한 부분
            ResultResponse resultResponse = ResultResponse.of(AuthResultCode.REGISTER_PRODUCT_SUCCESS,
                    Collections.singletonMap("DesignerEmail", portfolioDTO.getDesignerEmail()));
            //
            return new ResponseEntity<>(resultResponse, HttpStatus.OK);

        } catch (Exception e) { // service에서 throw된 에러 메세지 반환
            return ResponseEntity.internalServerError().body(e.getMessage());
        }

    }

    /**
     * 디자이너 이메일을 통해 포트폴리오 조회 메소드
     * @param requestData ( 디자이너 이메일)
     * @param bindingResult
     * @return
     */
    @Operation(summary = "포트폴리오 조회", description = "로그인 된 디자이너의 포트폴리오 정보를 조회합니다.")
    @GetMapping("/designer/portfolio")
    public ResponseEntity<Object> loadPortfolio(@Valid @RequestBody @Parameter(name = "designerEmail", description = "이메일 하나만 입력하세요.",
            example = "{'designerEmail':'sss@ssss.com'}") Map<String,String> requestData, BindingResult bindingResult) throws IOException {
        ResponseEntity<Object> errorResponse = getObjectResponseEntity(bindingResult.hasErrors(),
                bindingResult, ErrorCode.INVALID_BAD_REQUEST);
        if (errorResponse != null) return errorResponse;

        Optional<PortfolioInfoDTO> portfolioInfo = portfolioService.getPortfolio(requestData.get("designerEmail"));

        return portfolioInfo.<ResponseEntity<Object>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    /**
     * 에러 메세지 생성하는 메소드
     *
     * @param bindingResult
     * @param bindingResult1
     * @param invalidBadRequest
     * @return
     */
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
