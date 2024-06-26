package com.jjs.ClothingInventorySaleReformPlatform.domain.reform.controller;

import com.jjs.ClothingInventorySaleReformPlatform.domain.reform.dto.request.ClientResponse;
import com.jjs.ClothingInventorySaleReformPlatform.domain.reform.dto.request.EstimateRequestDTO;
import com.jjs.ClothingInventorySaleReformPlatform.domain.reform.dto.request.ReformOrderRequestDTO;
import com.jjs.ClothingInventorySaleReformPlatform.domain.reform.dto.response.EstimateResponseDTO;
import com.jjs.ClothingInventorySaleReformPlatform.domain.reform.dto.response.ReformOrderResponseDTO;
import com.jjs.ClothingInventorySaleReformPlatform.domain.reform.dto.response.ReformRequestResponseDTO;
import com.jjs.ClothingInventorySaleReformPlatform.global.common.returnResponse.Response;
import com.jjs.ClothingInventorySaleReformPlatform.domain.reform.service.ProgressManagementService;
import com.jjs.ClothingInventorySaleReformPlatform.domain.reform.service.EstimateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "리폼 수락, 견적서 작성", description = "리폼 수락 및 견적서 작성,수정 API 입니다.")
public class EstimateController {

    private final Response response;
    private final EstimateService estimateService;
    private final ProgressManagementService progressManagementService;

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
    @Operation(summary = "디자이너의 견적서 작성", description = "디자이너가 견적서를 작성 및 저장합니다. 저장 시 상태는 WRITING 입니다.")
    public ResponseEntity<?> saveEstimate(@ModelAttribute EstimateRequestDTO estimateRequestDTO, @PathVariable Long requestNumber) {
        try {
            estimateService.saveEstimate(estimateRequestDTO, requestNumber);
            return response.success("견적서 등록 성공.", HttpStatus.OK);
        } catch (Exception e) {
            log.error("리폼 요청 사항 저장 에러",e);
            return response.fail(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping(value = "/estimate/designer/estimateForm/{requestNumber}/submit")
    @Operation(summary = "디자이너의 견적서 제출", description = "디자이너가 견적서를 저장 및 수정 이후 최종 제출하는 단계로 제출 시, 구매자에게 노출됩니다.")
    public ResponseEntity<?> submitEstimate(@PathVariable Long requestNumber) {
        try {
            estimateService.submitEstimate(requestNumber);
            return response.success("견적서 제출 성공.", HttpStatus.OK);
        } catch (IllegalStateException e) {
            log.error("illegalStateException");
            return response.fail(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            log.error("제출 실패",e);
            return response.fail(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/estimate/designer/estimateForm/{requestNumber}")
    @Operation(summary = "디자이너의 견적서 조회", description = "디자이너가 견적서를 수정할 때, 기존의 정보를 불러오는 용도로 사용한다.")
    public ResponseEntity<?> getEstimateDetails(@PathVariable Long requestNumber) {
        try {
            EstimateResponseDTO estimateResponseDTO = estimateService.getEstimate(requestNumber);
            return response.success(estimateResponseDTO, "견적서 조회 성공", HttpStatus.OK);
        } catch (Exception e) {
            log.error("견적서 조회 실패", e);
            return response.fail("견적서 조회 실패", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/estimate/purchaser/estimateForm/{requestNumber}")
    @Operation(summary = "구매자의 견적서 조회", description = "구매자가 견적서를 조회할 때 사용. 디자이너가 제출한 견적서에 대해 조회 가능")
    public ResponseEntity<?> getPurEstimateDetails(@PathVariable Long requestNumber) {
        try {
            Optional<EstimateResponseDTO> estimateResponseDTO = estimateService.getPurEstimate(requestNumber);
            return response.success(estimateResponseDTO, "견적서 조회 성공", HttpStatus.OK);
        } catch (IllegalStateException e) {
            log.error("견적서가 존재하지 않습니다.");
            return response.fail(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            log.error("견적서 조회 실패", e);
            return response.fail("견적서 조회 실패", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    @PutMapping(value = "/estimate/designer/estimateForm/{estimateNumber}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "디자이너의 견적서 수정", description = "디자이너가 견적서를 수정한다. getEstimateDetails으로 조회하고 수정하면 전체 내용이 덮어쓰기 형식으로 저장된다.")
    public ResponseEntity<?> updateEstimateDetails(@PathVariable Long estimateNumber, @ModelAttribute EstimateRequestDTO estimateRequestDTO) {
        try {
            estimateService.updateEstimate(estimateRequestDTO, estimateNumber);
            return response.success("의뢰 수정 성공", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
            return response.fail("의뢰 수정 실패", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            return response.fail("의뢰 수정 실패", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            log.error(e.getMessage());
            return response.fail("의뢰 수정 실패", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/estimate/purchaser/{estimateNumber}/accept")
    @Operation(summary = "구매자의 견적서 수락 시작", description = "구매자는 견적서를 확인하고 수락하고 다음 배송정보 입력 창으로 넘어간다.")
    public ResponseEntity<?> selectEstimateAccept(@PathVariable Long estimateNumber) {
        try {
            estimateService.selEstimateAccept(estimateNumber);  // 수락 누르면 리품 주문 테이블 생성되고 상태가 주문중이 들어감
            return response.success("견적서 수락 및 주문 중 : 1", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
            return response.fail("견적서 수락 실패1", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            log.error(e.getMessage());
            return response.fail("견적서 수락 실패2", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping(value = "/estimate/purchaser/{estimateNumber}/reject")
    @Operation(summary = "구매자의 견적서 거절 완료", description = "구매자는 견적서를 확인하고 원치 않은 경우 거절한다.")
    public ResponseEntity<?> selectEstimateReject(@PathVariable Long estimateNumber) {
        try {
            estimateService.selEstimateReject(estimateNumber);  // 거절 버튼을 누르면 견적서 상태가 거절됨이 됨
            return response.success("견적서 거절 완료", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
            return response.fail("견적서 거절 실패1", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            log.error(e.getMessage());
            return response.fail("견적서 거절 실패2", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping(value = "/estimate/purchaser/acceptReformOrder/{estimateNumber}")
    @Operation(summary = "구매자의 견적서 수락 후 개인정보 입력", description = "구매자가 견적서 수락했을 때, 구매자는 배송 정보 및 개인 정보를 입력한다.")
    public ResponseEntity<?> estimateAcceptOrdering(@RequestBody ReformOrderRequestDTO reformOrderRequestDTO, @PathVariable Long estimateNumber) {
        try {
            estimateService.acceptOrdering(reformOrderRequestDTO, estimateNumber);
            return response.success("정보 입력 완료", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
            return response.fail("정보 입력 실패1", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            log.error(e.getMessage());
            return response.fail("정보 입력 실패2", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/estimate/purchaser/acceptReformOrder/{estimateNumber}")
    @Operation(summary = "구매자가 리폼 주문 비용(리폼 비용, 상품 가격, 총 가격) 조회", description = "구매자가 견적서의 개인 정보 입력 후 다음 페이지에서 총 리폼 주문 비용을 확인한다.")
    public ResponseEntity<?> getEstimatePrice(@PathVariable Long estimateNumber) {
        try {
            ReformOrderResponseDTO reformOrderResponseDTO = estimateService.getAcceptOrderingPrice(estimateNumber);
            return response.success(reformOrderResponseDTO, "리폼 비용 조회 성공", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
            return response.fail("리폼 비용 조회 실패1", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            log.error(e.getMessage());
            return response.fail("리폼 비용 조회 실패2", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 실행 전 -> estimate:REQUEST_WAITING, reformorders:ORDERING, progress_management:현재 생성X
     * 실행 후 -> estimate:REQUEST_ACCEPTED, reformorders:ORDER_COMPLETE, progress_management:REFORM_START
     * @param estimateNumber
     * @return
     */
    @PatchMapping(value = "/estimate/purchaser/acceptReformOrder/{estimateNumber}/complete")
    @Operation(summary = "견적서에 대한 리폼 및 상품을 최종적으로 결제 및 수락", description = "구매자는 리폼 주문 비용 확인 후 결제 및 수락 버튼(해당 API)으로 해당 견적서를 최종적으로 수락한다. 수락 시 형상관리 테이블에 초기 이미지가 저장된다.")
    public ResponseEntity<?> setEstimateAccept(@PathVariable Long estimateNumber) {
        try {
            estimateService.setAcceptComplete(estimateNumber);
            return response.success("리폼 결제 및 수락 완료", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
            return response.fail("리폼 결제 및 수락 실패1 (견적서 조회)", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            return response.fail("리폼 결제 및 수락 실패2 (수락된 의뢰가 아님)", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            log.error(e.getMessage());
            return response.fail("리폼 결제 및 수락 실패3", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}

