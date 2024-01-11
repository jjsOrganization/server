package com.jjs.ClothingInventorySaleReformPlatform.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "WATERSAVED", schema = "jjs")
public class Watersaved {  // 절약된 물
    @Id
    @Column(name = "PURCHASE_NUMBER", nullable = false)
    private Integer id;  // 구매 번호

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "PURCHASE_NUMBER", nullable = false)
    private Delivery delivery;  /** 아래 배송 상태 관련된거 인듯 **/

    @Size(max = 10)
    @NotNull
    @Column(name = "DELIVERY_STATUS", nullable = false, length = 10)
    private String deliveryStatus;  // 배송 상태

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "CATEGORY_NAME", nullable = false)
    private Category categoryName;  // 카테고리명

}