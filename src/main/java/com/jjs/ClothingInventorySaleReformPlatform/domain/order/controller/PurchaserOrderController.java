package com.jjs.ClothingInventorySaleReformPlatform.domain.order.controller;

import com.jjs.ClothingInventorySaleReformPlatform.domain.order.dto.response.PurchaserOrderDTO;
import com.jjs.ClothingInventorySaleReformPlatform.global.common.returnResponse.Response;
import com.jjs.ClothingInventorySaleReformPlatform.domain.order.service.DeliveryService;
import com.jjs.ClothingInventorySaleReformPlatform.domain.order.service.PurchaserOrderService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PurchaserOrderController {

    private final PurchaserOrderService purchaserOrderService;
    private final DeliveryService deliveryService;
    private final Response response;

    @GetMapping("/order/purchaser-list")
    @Operation(summary = "구매자 주문 목록 조회", description = "구매자가 구매한 상품들을 목록으로 조회한다.")
    public ResponseEntity<?> getPurchaserOrders(Authentication authentication) {
        String purchaserEmail = authentication.getName();
        List<PurchaserOrderDTO> orders = purchaserOrderService.getPurchaserOrders(purchaserEmail);
        return response.success(orders, "구매 목록 조회 완료", HttpStatus.OK);
    }
}
