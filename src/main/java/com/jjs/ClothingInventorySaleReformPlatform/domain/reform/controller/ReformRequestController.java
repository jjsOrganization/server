package com.jjs.ClothingInventorySaleReformPlatform.domain.reform.controller;

import com.jjs.ClothingInventorySaleReformPlatform.domain.reform.dto.response.GetEstimateNumberResponseDTO;
import com.jjs.ClothingInventorySaleReformPlatform.domain.reform.dto.response.ReformProductInfoDTO;
import com.jjs.ClothingInventorySaleReformPlatform.domain.reform.dto.response.ReformRequestCheckPurchaserDTO;
import com.jjs.ClothingInventorySaleReformPlatform.domain.reform.dto.request.ReformRequestDTO;
import com.jjs.ClothingInventorySaleReformPlatform.global.common.returnResponse.Response;
import com.jjs.ClothingInventorySaleReformPlatform.global.error.ErrorCode;
import com.jjs.ClothingInventorySaleReformPlatform.global.error.ErrorResponse;
import com.jjs.ClothingInventorySaleReformPlatform.domain.reform.service.ReformRequestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@Getter
@Setter
@RequiredArgsConstructor
@Slf4j
@Tag(name = "리폼 의뢰 신청,수정", description = "구매자가 디자이너에게 리폼 신청및 수정하는 API 입니다.")
public class ReformRequestController {
    private final ReformRequestService reformRequestService;
    private final Response response;

    // 사용 안하는 API
//    @GetMapping("/reform-request/purchaser/creation/{itemId}")
//    @Operation(summary = "리폼 의뢰 페이지 이동", description = "리폼 의뢰를 위한 폼을 불러옵니다.")
//    public ResponseEntity<?> getReformRequestForm(@Valid @PathVariable Long itemId) {
//
//        try{
//            ReformProductInfoDTO reformProductInfoDTO = reformRequestService.getProductInfo(itemId);
//            return response.success(reformProductInfoDTO, "리폼 페이지에 접속했습니다.", HttpStatus.OK);
//
//        }catch(Exception e){
//            return response.fail("상품 정보를 찾을 수 없습니다.", HttpStatus.BAD_REQUEST);
//        }
//    }

    @PostMapping(value = "/reform-request/purchaser/creation/{itemId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "리폼 요청 사항 저장", description = "리폼 요청 사항을 저장합니다.")

    public ResponseEntity<?> sendReformRequest(@ModelAttribute ReformRequestDTO reformRequestDTO,  BindingResult bindingResult,
                                               @PathVariable Long itemId) throws Exception {
        try {
            reformRequestService.saveReformRequest(reformRequestDTO, itemId);
            return response.success("의뢰 성공.", HttpStatus.OK);

        } catch (Exception e) {
            log.error("리폼 요청 사항 저장 에러",e);
            return response.fail(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/reform-request/purchaser/modification/{requestId}")
    @Operation(summary = "리폼 수정 페이지 이동", description = "리폼 수정 페이지로 이동합니다. ")
    public ResponseEntity<?> getStoredReformRequestForm(@PathVariable Long requestId) {
        try {
            ReformRequestDTO reformRequestForm = reformRequestService.getReformRequestForm(requestId);
            return response.success(reformRequestForm, "저장된 데이터", HttpStatus.OK);
        } catch (RuntimeException e) {
            return response.fail("이미 진행중입니다.", HttpStatus.CONFLICT);
        } catch (Exception e) {
            log.error("저장된 리폼 정보 가져오기 에러", e);
            return response.fail(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(value = "/reform-request/purchaser/modification/{requestId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "리폼 수정 사항 저장", description = "리폼 수정 사항을 저장합니다. ")
    public ResponseEntity<?> updateReformRequest(@ModelAttribute ReformRequestDTO reformRequestDTO, BindingResult bindingResult,
                                                 @PathVariable Long requestId) throws Exception {
        try {
            reformRequestService.updateReformRequest(reformRequestDTO, requestId);
            return response.success("의뢰 수정 성공", HttpStatus.OK);
        } catch (Exception e) {
            log.error("리폼 의뢰 수정 에러",e);
            return response.fail(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/reform/purchaser/requests/all")
    @Operation(summary = "구매자가 요청한 의뢰 내역 전체 조회", description = "구매자가 리폼 요청을 한 내역들을 조회한다.")
    public ResponseEntity<?> getAllRequests() {
        List<ReformRequestCheckPurchaserDTO> reformRequestCheckDTOList = reformRequestService.getAllRequestList();

        return response.success(reformRequestCheckDTOList, "구매자 요청 의뢰 내역 조회 완료", HttpStatus.OK);
    }

    @GetMapping("/returnEstimateNumber/{reformRequest}")
    @Operation(summary = "요청서id로 견적서id 반환 Api")
    public ResponseEntity<?> getEstimateNumber(@PathVariable Long reformRequest) {

        GetEstimateNumberResponseDTO getEstimateNumberResponseDTO = reformRequestService.getEstimateNumber(reformRequest);
        return response.success(getEstimateNumberResponseDTO, "조회 성공", HttpStatus.OK);
    }

//    private static ResponseEntity<Object> getObjectResponseEntity(boolean bindingResult, BindingResult bindingResult1, ErrorCode invalidBadRequest) {
//        if (bindingResult) {
//            List<ErrorResponse.FieldError> fieldErrors = bindingResult1.getFieldErrors().stream()
//                    .map(fieldError -> new ErrorResponse.FieldError(
//                            fieldError.getField(),
//                            fieldError.getRejectedValue() == null ? "" : fieldError.getRejectedValue().toString(),
//                            fieldError.getDefaultMessage()))
//                    .collect(Collectors.toList());
//
//            ErrorResponse errorResponse = new ErrorResponse(invalidBadRequest, fieldErrors);
//            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
//        }
//        return null;
//    }

}
