package com.jjs.ClothingInventorySaleReformPlatform.domain.order.dto.response;

import com.jjs.ClothingInventorySaleReformPlatform.domain.order.entity.Delivery;
import com.jjs.ClothingInventorySaleReformPlatform.domain.order.entity.DeliveryStatus;
import com.jjs.ClothingInventorySaleReformPlatform.domain.order.entity.Order;
import com.jjs.ClothingInventorySaleReformPlatform.domain.order.entity.OrderDetail;
import com.jjs.ClothingInventorySaleReformPlatform.domain.order.entity.OrderStatus;
import com.jjs.ClothingInventorySaleReformPlatform.domain.product.entity.Product;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PurchaserOrderDTO {

    private Long orderId;  // 주문 번호
    private LocalDateTime orderDate;  // 주문일
    private OrderStatus orderStatus;  // 주문 상태
    private DeliveryStatus deliveryStatus;  // 배송 상태

    private Long orderDetailId;  // 주문 상세 번호 -> 주문 상품 번호
    private Long productId;  // 상품 번호
    private String productName;  // 상품 이름
    private int quantity;  // 상품 개수
    private int price;  // 상품 가격 -> 상품(1개)가격 * 상품 개수
    private String imgUrl;

    public PurchaserOrderDTO(Order order, OrderDetail orderDetail, Product product, Delivery delivery) {

        this.orderId = order.getOrderId();
        this.orderDate = order.getOrderDate();
        this.orderStatus = order.getOrderStatus();
        this.deliveryStatus = delivery.getDeliveryStatus();

        this.orderDetailId = orderDetail.getOrderDetailId();
        this.productId = product.getId();
        this.productName = product.getProductName();
        this.quantity = orderDetail.getQuantity();
        this.price = orderDetail.getPrice();
        this.imgUrl = product.getProductImg().get(0).getImgUrl();
    }

}
