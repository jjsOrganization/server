package com.jjs.ClothingInventorySaleReformPlatform.controller.order;

import com.jjs.ClothingInventorySaleReformPlatform.dto.order.response.SellerOrderDTO;
import com.jjs.ClothingInventorySaleReformPlatform.dto.response.Response;
import com.jjs.ClothingInventorySaleReformPlatform.service.order.SellerOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class SellerOrderController {

    private final SellerOrderService sellerOrderService;
    private final Response response;

    @GetMapping("/order/list")
    public ResponseEntity<?> getSellerOrders(Authentication authentication) {
        String sellerEmail = authentication.getName();
        List<SellerOrderDTO> orders = sellerOrderService.getSellerOrders(sellerEmail);
        return response.success(orders, "판매 목록 조회 완료", HttpStatus.OK);
    }
}
