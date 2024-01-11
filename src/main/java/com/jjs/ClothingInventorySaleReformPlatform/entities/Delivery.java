package com.jjs.ClothingInventorySaleReformPlatform.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "DELIVERY", schema = "jjs")
public class Delivery {  // 배송
    @Id
    @Column(name = "PURCHASE_NUMBER", nullable = false)
    private Integer id;  // 구매 번호

    @Size(max = 10)
    @NotNull
    @Column(name = "DELIVERY_STATUS", nullable = false, length = 10)
    private String DeliveryStatus;  // 배송 상태

    @OneToOne(mappedBy = "delivery")
    private Purchaselist purchaselist;

    @OneToOne(mappedBy = "delivery")
    private Watersaved watersaved;

}