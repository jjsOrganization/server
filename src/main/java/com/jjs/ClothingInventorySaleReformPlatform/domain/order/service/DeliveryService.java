package com.jjs.ClothingInventorySaleReformPlatform.domain.order.service;

import com.jjs.ClothingInventorySaleReformPlatform.domain.order.entity.Delivery;
import com.jjs.ClothingInventorySaleReformPlatform.domain.order.entity.DeliveryStatus;
import com.jjs.ClothingInventorySaleReformPlatform.domain.order.entity.Order;
import com.jjs.ClothingInventorySaleReformPlatform.domain.order.repository.DeliveryRepository;
import com.jjs.ClothingInventorySaleReformPlatform.domain.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final OrderRepository orderRepository;

    /**
     * 배송 초기 상태는 WAITING으로 설정
     */
    @Transactional
    public void createDelivery(String purchaserEmail) {
        /*
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("주문 정보를 찾을 수 없습니다."));
         */

        // 현재 로그인한 사용자(구매자)의 가장 최근 주문을 조회
        Order order = orderRepository.findTopByPurchaserInfoEmailOrderByOrderDateDesc(purchaserEmail)
                .orElseThrow(() -> new IllegalArgumentException("주문 정보를 찾을 수 없습니다."));

        Delivery delivery = new Delivery();
        delivery.setOrder(order);
        delivery.setDeliveryStatus(DeliveryStatus.WAITING); // 초기 배송 상태 설정
        deliveryRepository.save(delivery);

    }

    /**
     * 판매자가 배송 상태를 변경할 때 호출되어, 지정된 배송 정보의 상태를 업데이트한다.
     * 판매자 - 판매자 목록 조회에서 구매자의 주문을 판매자가 배송 상태를 변경하는 경우
     * @param orderId
     * @param newStatus
     */
    @Transactional
    public void updateDeliveryStatus(Long orderId, DeliveryStatus newStatus) {
        // 주문 ID를 기반으로 주문 정보 조회
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("해당 주문을 찾을 수 없습니다."));

        // 해당 주문과 연결된 배송 정보가 있는지 확인
        Delivery delivery = order.getDelivery();
        if (delivery == null) {
            throw new IllegalArgumentException("해당 주문에 대한 배송 정보가 없습니다.");
        }

        // 배송 상태 업데이트
        delivery.setDeliveryStatus(newStatus);
        // 저장을 위해 Order 객체를 저장하는 것으로 충분 (Cascade 설정에 따라 Delivery가 자동으로 업데이트 될 수 있음)
        orderRepository.save(order);
    }
}
