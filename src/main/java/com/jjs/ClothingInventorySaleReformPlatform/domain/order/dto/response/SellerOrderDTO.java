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
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class SellerOrderDTO {


    private Long orderId;  // 주문 번호
    private LocalDateTime orderDate;  // 주문일
    private String purchaserEmail;  // 구매자 이메일
    private List<OrderDetailDTO> orderDetails;  // 주문 상세 정보(주문에 포함된 상품들)
    private String postcode;  // 구매자 우편번호
    private String address;  // 구매자 주소
    private String detailAddress;  // 구매자 상세 주소
    private String phoneNumber;  // 구매자 전화번호
    private String deliveryRequest;  // 배송 요청사항
    private int totalPrice;  // 총 가격
    private OrderStatus orderStatus;  // 주문 상태
    private DeliveryStatus deliveryStatus;  // 배송 상태

    public SellerOrderDTO(Order order, OrderDetail orderDetail, Product product, Delivery delivery) {
        this.orderId = order.getOrderId();
        this.purchaserEmail = order.getPurchaserInfo().getEmail();
        this.orderDate = order.getOrderDate();
        this.postcode = order.getPostcode();
        this.address = order.getAddress();
        this.detailAddress = order.getDetailAddress();
        this.phoneNumber = order.getPhoneNumber();
        this.deliveryRequest = order.getDeliveryRequest();
        this.totalPrice = order.getTotalPrice();
        this.orderStatus = order.getOrderStatus();
        this.deliveryStatus = delivery.getDeliveryStatus();

        // 리스트 초기화
        this.orderDetails = new ArrayList<>();

        OrderDetailDTO orderDetailDTO = new OrderDetailDTO();
        orderDetailDTO.setOrderDetailId(orderDetail.getOrderDetailId());
        orderDetailDTO.setProductId(product.getId());
        orderDetailDTO.setProductName(product.getProductName());
        orderDetailDTO.setQuantity(orderDetail.getQuantity());
        orderDetailDTO.setPrice(orderDetail.getPrice());
        orderDetailDTO.setImgUrl(product.getProductImg().get(0).getImgUrl());
        this.orderDetails.add(orderDetailDTO);
    }

    @Getter
    @Setter
    public static class OrderDetailDTO {
        private Long orderDetailId;  // 주문 상세 번호
        private Long productId;  // 상품 번호
        private String productName;  // 상품 이름
        private int quantity;  // 상품 개수
        private int price;  // 상품 가격
        private String imgUrl;  // 해당 상품 이미지
    }
}
