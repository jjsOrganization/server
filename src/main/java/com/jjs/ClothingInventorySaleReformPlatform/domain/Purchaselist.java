package com.jjs.ClothingInventorySaleReformPlatform.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "PURCHASELIST", schema = "jjs")
public class Purchaselist {  // 구매리스트
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PURCHASE_NUMBER", nullable = false)
    private Integer id;  // 구매 번호

    /**
     * 구매 번호 중복 이거 아직 뭔지 모름.
     */
    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "PURCHASE_NUMBER", nullable = false)
    private Delivery delivery;  // 구매 번호

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "PRODUCT_NUMBER", nullable = false)
    private Product productNumber;  // 상품 번호

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "SELLER_EMAIL", nullable = false)
    private Purchaser sellerEmail;  // 구매자 이메일

    @NotNull
    @Column(name = "ORDER_STATUS", nullable = false)
    private Character orderStatus;  // 주문 상태

    @Size(max = 10)
    @NotNull
    @Column(name = "DELIVERY_STATUS", nullable = false, length = 10)
    private String deliveryStatus;  // 배송 상태

}