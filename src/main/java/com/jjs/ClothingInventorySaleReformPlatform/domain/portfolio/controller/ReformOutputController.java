package com.jjs.ClothingInventorySaleReformPlatform.domain.portfolio.controller;

import com.jjs.ClothingInventorySaleReformPlatform.domain.portfolio.dto.request.ReformOutputDTO;
import com.jjs.ClothingInventorySaleReformPlatform.domain.portfolio.dto.response.EditContentsReformOutputDTO;
import com.jjs.ClothingInventorySaleReformPlatform.domain.portfolio.dto.response.FixedReformOutputDTO;
import com.jjs.ClothingInventorySaleReformPlatform.domain.portfolio.entity.ReformOutput;
import com.jjs.ClothingInventorySaleReformPlatform.domain.portfolio.service.ReformOutputService;
import com.jjs.ClothingInventorySaleReformPlatform.domain.reform.dto.request.ReformRequestDTO;
import com.jjs.ClothingInventorySaleReformPlatform.global.common.returnResponse.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "디자이너 포트폴리오 작업물", description = "포트폴리오 작업물 API 입니다.")
public class ReformOutputController {

    private final Response response;
    private final ReformOutputService reformOutputService;

    @Operation(summary = "포트폴리오 작업물 등록", description = "디자이너의 리폼 진행이 끝나면 형상관리 id로 작업물들을 등록한다.")
    @PostMapping(value = "/portfolio/reformOutput/upload/{progressNumber}")
    public ResponseEntity<?> uploadReformOutput(@Valid @RequestBody ReformOutputDTO reformOutputDTO, @PathVariable Long progressNumber) {
        try {
            reformOutputService.saveReformOutput(reformOutputDTO, progressNumber);
            return response.success("포트폴리오 작업물 등록 성공.", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            log.error("IllegalArgumentException");
            return response.fail(e.getMessage(), HttpStatus.CONFLICT);
        } catch (Exception e) {
            log.error("포트폴리오 작업물 저장 실페",e);
            return response.fail(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "포트폴리오 작업물 등록 및 수정 시, 고정 항목 조회", description = "디자이너가 작업물 등록 시, 직접 작성하는 항목을 제외한 나머지 항목들을 확인할 수 있다.")
    @GetMapping(value = "/portfolio/reformOutput/upload/{progressNumber}")
    public ResponseEntity<?> getUploadReformOutput(@PathVariable Long progressNumber) {
        try {
            FixedReformOutputDTO fixedReformOutputDTO = reformOutputService.getReformOutputByProgressId(progressNumber);
            return response.success(fixedReformOutputDTO, "포드폴리오 작업물 등록 시, 고정 항목 조회 완료", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            log.error("IllegalArgumentException");
            return response.fail(e.getMessage(), HttpStatus.CONFLICT);
        } catch (Exception e) {
            log.error("Exception", e);
            return response.fail(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "작업물 수정 시, 이전에 작성했던 정보(제목, 내용) 조회", description = "디자이너가 작업물 수정을 하기위해 수정 페이지에 왔을 때, 전에 작성했던 내용을 수정해야되기 때문에 이전에 작성했던 내용들을 불러와야 한다.")
    @GetMapping(value = "/portfolio/reformOutput/edit/{progressNumber}")
    public ResponseEntity<?> getEditContentsReformOutput(@PathVariable Long progressNumber) {
        try {
            EditContentsReformOutputDTO editContentsReformOutputDTO = reformOutputService.getEditContentsByProgressId(progressNumber);
            return response.success(editContentsReformOutputDTO, "포드폴리오 작업물 등록 시, 변동 항목 조회 완료", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            log.error("IllegalArgumentException");
            return response.fail(e.getMessage(), HttpStatus.CONFLICT);
        } catch (Exception e) {
            log.error("Exception", e);
            return response.fail(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
