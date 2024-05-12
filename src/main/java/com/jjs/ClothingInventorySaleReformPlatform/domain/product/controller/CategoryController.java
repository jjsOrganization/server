package com.jjs.ClothingInventorySaleReformPlatform.domain.product.controller;

import com.jjs.ClothingInventorySaleReformPlatform.domain.product.dto.response.SaveWaterDTO;
import com.jjs.ClothingInventorySaleReformPlatform.domain.product.service.CategoryService;
import com.jjs.ClothingInventorySaleReformPlatform.global.common.returnResponse.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
@Tag(name = "아낀 물의 양", description = "카테고리에 대한 아낀 물의 양을 집계하는 Controller 입니다.")
public class CategoryController {

    private final CategoryService categoryService;
    private final Response response;

    @Operation(summary = "각 카테고리 별 아낀 물의 양의 총 합계를 반환하는 API", description = "각 카테고리 별 아낀 물의 양의 총 합계를 반환한다.")
    @GetMapping("/calculate/saveWater")
    public ResponseEntity<?> calSaveWater() {
        SaveWaterDTO saveWaterDTO = categoryService.calculateWaterUsage();
        return response.success(saveWaterDTO, "아낀 물의 양 반환 성공", HttpStatus.OK);
    }

}
