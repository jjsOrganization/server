package com.jjs.ClothingInventorySaleReformPlatform.service;

import com.jjs.ClothingInventorySaleReformPlatform.domain.delivery.Delivery;
import com.jjs.ClothingInventorySaleReformPlatform.domain.delivery.DeliveryStatus;
import com.jjs.ClothingInventorySaleReformPlatform.domain.order.Order;
import com.jjs.ClothingInventorySaleReformPlatform.repository.DeliveryRepository;
import com.jjs.ClothingInventorySaleReformPlatform.repository.order.OrderRepository;
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
     * @param orderId
     */
    @Transactional
    public void createDelivery(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("주문 정보를 찾을 수 없습니다."));
        Delivery delivery = new Delivery();
        delivery.setOrder(order);
        delivery.setDeliveryStatus(DeliveryStatus.WAITING); // 초기 배송 상태 설정
        deliveryRepository.save(delivery);

    }

    /**
     * 판매자가 배송 상태를 변경할 때 호출되어, 지정된 배송 정보의 상태를 업데이트한다.
     * @param deliveryId
     * @param status
     */
    @Transactional
    public void updateDeliveryStatus(Long deliveryId, DeliveryStatus status) {
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new IllegalArgumentException("배송 정보를 찾을 수 없습니다."));

        delivery.setDeliveryStatus(status);
        deliveryRepository.save(delivery);
    }
}
