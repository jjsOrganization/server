package com.jjs.ClothingInventorySaleReformPlatform.controller;

import com.jjs.ClothingInventorySaleReformPlatform.dto.reform.request.ProgressImgRequestDTO;
import com.jjs.ClothingInventorySaleReformPlatform.dto.response.Response;
import com.jjs.ClothingInventorySaleReformPlatform.repository.ProgressRepository;
import com.jjs.ClothingInventorySaleReformPlatform.service.ProgressManagementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "리폼 형상관리", description = "구매자가 디자이너가 제공한 견적서 수락 시, 진행되는 형상관리에 대한 API 입니다.")
public class ProgressManagementController {

    private final Response response;
    private final ProgressRepository progressRepository;
    private final ProgressManagementService progressManagementService;

    @PatchMapping("/progress/designer/setImg/first")
    @Operation(summary = "형상관리 첫 번째 이미지 등록", description = "형상관리 시, 첫 번째의 리폼 중간 이미지를 등록하는 API 입니다.")
    public ResponseEntity<?> setFirstImg(ProgressImgRequestDTO imgRequestDTO) {
        try {
            progressManagementService.saveFirstImg(imgRequestDTO);
            return response.success(imgRequestDTO.getEstimateId(), "해당 견적서와 매칭되는 형상관리에 첫 번째 사진 등록 완료", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
            return response.fail("이미지 등록 실패", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            log.error(e.getMessage());
            return response.fail("이미지 등록 실패", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/progress/designer/setImg/second")
    @Operation(summary = "형상관리 두 번째 이미지 등록", description = "형상관리 시, 두 번째의 리폼 중간 이미지를 등록하는 API 입니다.")
    public ResponseEntity<?> setSecondImg(ProgressImgRequestDTO imgRequestDTO) {
        try {
            progressManagementService.saveSecondImg(imgRequestDTO);
            return response.success(imgRequestDTO.getEstimateId(), "해당 견적서와 매칭되는 형상관리에 두 번째 사진 등록 완료", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
            return response.fail("이미지 등록 실패", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            log.error(e.getMessage());
            return response.fail("이미지 등록 실패", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/progress/designer/setImg/complete")
    @Operation(summary = "형상관리 완료된 이미지 등록", description = "형상관리 시, 리폼 완료된 이미지를 등록하는 API 입니다.")
    public ResponseEntity<?> setCompleteImg(ProgressImgRequestDTO imgRequestDTO) {
        try {
            progressManagementService.saveCompleteImg(imgRequestDTO);
            return response.success(imgRequestDTO.getEstimateId(), "해당 견적서와 매칭되는 형상관리에 리폼 완료된 사진 등록 완료", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
            return response.fail("이미지 등록 실패", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            log.error(e.getMessage());
            return response.fail("이미지 등록 실패", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
