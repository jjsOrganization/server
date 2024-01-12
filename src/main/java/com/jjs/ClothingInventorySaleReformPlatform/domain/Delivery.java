package com.jjs.ClothingInventorySaleReformPlatform.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "DELIVERY")
public class Delivery {  // 배송

    // 구매번호, 배송상태
    @Id
    @Column(name = "PURCHASE_NUMBER", nullable = false)
    private Integer id;  // 구매 번호

    @Size(max = 10)
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "DELIVERY_STATUS", nullable = false, length = 10)
    private DeliveryStatus deliveryStatus;  // 배송 상태 [배송중, 배송완료, 배송시작, 준비중]

/*
    @OneToOne(mappedBy = "delivery")
    private Purchaselist purchaselist;

    @OneToOne(mappedBy = "delivery")
    private Watersaved watersaved;
*/
}