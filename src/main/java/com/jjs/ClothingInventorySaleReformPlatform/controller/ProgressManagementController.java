package com.jjs.ClothingInventorySaleReformPlatform.controller;

import com.jjs.ClothingInventorySaleReformPlatform.dto.response.Response;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "리폼 형상관리", description = "구매자가 디자이너가 제공한 견적서 수락 시, 진행되는 형상관리에 대한 API 입니다.")
public class ProgressManagementController {

    private final Response response;


}
