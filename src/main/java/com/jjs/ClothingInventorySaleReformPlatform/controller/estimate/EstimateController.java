package com.jjs.ClothingInventorySaleReformPlatform.controller.estimate;

import com.jjs.ClothingInventorySaleReformPlatform.dto.estimate.ClientResponse;
import com.jjs.ClothingInventorySaleReformPlatform.dto.estimate.request.EstimateRequestDTO;
import com.jjs.ClothingInventorySaleReformPlatform.dto.reformrequest.ReformRequestResponseDTO;
import com.jjs.ClothingInventorySaleReformPlatform.dto.response.Response;
import com.jjs.ClothingInventorySaleReformPlatform.service.estimate.EstimateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "리폼 수락, 견적서 작성", description = "리폼 수락 및 견적서 작성,수정 API 입니다.")
public class EstimateController {

    private final Response response;
    private final EstimateService estimateService;

    @GetMapping("/estimate/designer/requestForm")
    @Operation(summary = "디자이너 요청받은 의뢰 조회", description = "디자이너가 요청받은 모든 의뢰를 조회합니다.")
    public ResponseEntity<?> getAllRequestForm() {
        List<ReformRequestResponseDTO> reformRequestResponseDTOList = estimateService.getAllRequestList();

        return response.success(reformRequestResponseDTOList);
    }

    @GetMapping("/estimate/designer/requestForm/{requestNumber}")
    @Operation(summary = "요청받은 의뢰 상세 페이지 이동", description = "요청받은 의뢰의 상세 내역을 조회합니다.")
    public ResponseEntity<?> getRequestFormByNumber(@PathVariable Long requestNumber){
        List<ReformRequestResponseDTO> requestListByNumber = estimateService.getRequestListByNumber(requestNumber);

        return response.success(requestListByNumber);
    }

    @PostMapping("/estimate/designer/requestForm/{requestNumber}")
    @Operation(summary = "요청받은 의뢰 상태 변경", description = "요청받은 의뢰의 상태를 변경합니다(수락 or 거절)")
    public ResponseEntity<?> updateRequestStatus(@PathVariable Long requestNumber, @RequestBody ClientResponse clientResponse) throws Exception {
        try{
            estimateService.updateRequestStatus(requestNumber,clientResponse.getClientResponse());
        }catch (IllegalArgumentException e){
            log.error(e.getMessage());
            return response.fail("상태 변경 실패", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return response.success("상태 변경 완료.");
    }

    @PostMapping(value = "/estimate/designer/estimateForm/{requestNumber}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "디자이너의 견적서 작성", description = "디자이너가 견적서를 작성합니다.")
    public ResponseEntity<?> sendEstimate(@ModelAttribute EstimateRequestDTO estimateRequestDTO, @PathVariable Long requestNumber) {
        try {
            estimateService.saveEstimate(estimateRequestDTO, requestNumber);
            return response.success("견적서 등록 성공.", HttpStatus.OK);
        } catch (Exception e) {
            log.error("리폼 요청 사항 저장 에러",e);
            return response.fail(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



}

