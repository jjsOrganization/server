package com.jjs.ClothingInventorySaleReformPlatform.domain.reform.order;

import com.jjs.ClothingInventorySaleReformPlatform.domain.reform.estimate.Estimate;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class)
@Entity
@Getter
@Table(name = "REFORMORDERS")
@Setter
public class ReformOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long orderId;  // 주문 번호

    @OneToOne(mappedBy = "ESTIMATE_NUMBER", fetch = FetchType.LAZY)
    private Estimate estimate;

    @CreatedDate
    private LocalDateTime orderDate;  // 주문일
    private String postcode;  // 우편번호
    private String address;  // 주소
    private String detailAddress;  // 상세주소
    private String phoneNumber;  // 휴대폰 번호
    private String deliveryRequest;  // 배송 요청사항
    private int totalPrice;  // 총 주문 금액

    @Enumerated(EnumType.STRING)
    private ReformOrderStatus orderStatus;  // 주문 상태


}
