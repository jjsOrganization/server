package com.jjs.ClothingInventorySaleReformPlatform.domain.order.controller;

import com.jjs.ClothingInventorySaleReformPlatform.domain.order.entity.DeliveryStatus;
import com.jjs.ClothingInventorySaleReformPlatform.domain.order.dto.response.SellerOrderDTO;
import com.jjs.ClothingInventorySaleReformPlatform.global.common.returnResponse.Response;
import com.jjs.ClothingInventorySaleReformPlatform.domain.order.service.DeliveryService;
import com.jjs.ClothingInventorySaleReformPlatform.domain.order.service.SellerOrderService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class SellerOrderController {

    private final SellerOrderService sellerOrderService;
    private final DeliveryService deliveryService;
    private final Response response;

    @GetMapping("/order/seller-list")
    @Operation(summary = "판매자의 상품 주문 목록 조회", description = "구매자가 상품을 구매 완료하면 주문을 확인하고 상품을 배송해야 한다. 판매자가 판매하는 상품이 판매되면 생성되는 주문들의 리스트로 주문 정보, 구매자 정보(주소, 배송 정보 등), 상품 정보 등이 조회된다.")
    public ResponseEntity<?> getSellerOrders(Authentication authentication) {
        String sellerEmail = authentication.getName();
        List<SellerOrderDTO> orders = sellerOrderService.getSellerOrders(sellerEmail);
        return response.success(orders, "판매 목록 조회 완료", HttpStatus.OK);
    }


    @PatchMapping("/order/seller-list/{orderId}/delivery-status")
    @Operation(summary = "판매자의 주문 목록에서 배송 상태 변경", description = "판매자는 주문 목록에서 해당 주문에 대하여 드롭다운을 이용해서 배송 상태를 변경한다. /order/seller-list/12/delivery-status?status=DELIVERED 로 변경 가능. status=(WAITING, DELIVER_START, DELIVERING, DELIVER_COMPLETE) 중 선택")
    public ResponseEntity<?> updateDeliveryStatus(@PathVariable Long orderId, @RequestParam DeliveryStatus status) {
        deliveryService.updateDeliveryStatus(orderId, status);

        return response.success(orderId, "배송 상태 변경", HttpStatus.OK);
    }
}
