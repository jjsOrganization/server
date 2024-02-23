package com.jjs.ClothingInventorySaleReformPlatform.domain.delivery;

import com.jjs.ClothingInventorySaleReformPlatform.domain.order.Order;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "DELIVERY")
@EntityListeners(AuditingEntityListener.class)
public class Delivery {  // 배송

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "delivery_id")
    private Long deliveryId;  // 배송 번호

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Enumerated(EnumType.STRING)  // 문자를 넣기 위해 EnumType.String 사용(필수!!!!)
    private DeliveryStatus deliveryStatus;  // 배송 상태 [배송중, 배송완료, 배송시작, 준비중]

    @CreatedDate
    private LocalDateTime orderDate;  // 주문 완료일

}