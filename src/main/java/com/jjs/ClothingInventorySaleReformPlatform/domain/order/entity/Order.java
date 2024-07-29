package com.jjs.ClothingInventorySaleReformPlatform.domain.order.entity;

import com.jjs.ClothingInventorySaleReformPlatform.domain.user.entity.PurchaserInfo;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@EntityListeners(AuditingEntityListener.class)
@Entity
@Getter
@Table(name = "ORDERS")
@Setter
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long orderId;  // 주문 번호

    @ManyToOne
    @JoinColumn(name = "email")
    private PurchaserInfo purchaserInfo;  // 구매자 이메일 - 회원 번호 대체

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<OrderDetail> orderDetails = new ArrayList<>();

    @OneToOne(mappedBy = "order", fetch = FetchType.LAZY)
    private Delivery delivery;

    @CreatedDate
    private LocalDateTime orderDate;  // 주문일
    private String postcode;  // 우편번호
    private String address;  // 주소
    private String detailAddress;  // 상세주소
    private String phoneNumber;  // 휴대폰 번호
    private String deliveryRequest;  // 배송 요청사항
    private int totalPrice;  // 총 주문 금액

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;  // 주문 상태

    public void addOrderDetail(OrderDetail orderDetail) {
        this.orderDetails.add(orderDetail);
    }
}
