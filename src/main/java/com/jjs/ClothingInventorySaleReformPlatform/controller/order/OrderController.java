package com.jjs.ClothingInventorySaleReformPlatform.controller.order;

import com.jjs.ClothingInventorySaleReformPlatform.dto.order.OrderDTO;
import com.jjs.ClothingInventorySaleReformPlatform.dto.response.Response;
import com.jjs.ClothingInventorySaleReformPlatform.service.order.DeliveryService;
import com.jjs.ClothingInventorySaleReformPlatform.service.order.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "상품 구매 컨트롤러", description = "상품 구매 API 입니다.")
public class OrderController {

    private final OrderService orderService;
    private final DeliveryService deliveryService;
    private final Response response;

    @PostMapping("/cart/purchaser/order")
    @Operation(summary = "장바구니에서 상품 구매(상품 구매 진행 중)", description = "장바구니에서 선택한 상품들(1개 이상)에 대하여 상품 구매가 진행된다. 주문자에 대한 정보 작성 이전 단계로 orders와 order_detail 테이블에 정보가 담기는 첫 단계이다.")
    public ResponseEntity<?> createOrder(@RequestBody OrderDTO orderDTO) {
        try {
            Long orderId = orderService.createOrder(orderDTO);
            return response.success(orderId, "상품 구매 중", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
          log.error("Order post fail: ", e);
          return response.fail(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return response.fail("주문 생성 중 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PutMapping("/cart/purchaser/order/{orderId}")
    @Operation(summary = "장바구니에서 상품 구매(상품 구매 완료)", description = "createOrder 이후에 주문자에 대한 정보를 기입 완료 후 최종 구매 완료할 때 사용한다. createOrder에서 생성했던 테이블에 빈칸의 정보들을 채워 넣는다.")
    public ResponseEntity<?> updateOrder(@PathVariable Long orderId, @RequestBody OrderDTO orderDTO) {
        try {
            orderService.updateOrder(orderId, orderDTO);  // 상품 구매
            orderService.decreaseProductStock(orderId);  // 상품 재고 감소
            deliveryService.createDelivery(orderId); // 배송 정보 초기화
            return response.success( "상품 구매가 완료되었습니다.", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            log.error("Order update failed: ", e);
            return response.fail(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error("Unexpected error during order update: ", e);
            return response.fail("주문 업데이트 중 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
