package com.jjs.ClothingInventorySaleReformPlatform.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "CART", schema = "jjs")
public class Cart {  // 장바구니
    @Id
    @Column(name = "ID")
    private Integer id;  // 쓸모없는 컬럼

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "PURCHASER_EMAIL", nullable = false)
    private Purchaser purchaserEmail;  // 구매자 이메일

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "PRODUCT_NUMBER", nullable = false)
    private Product productNumber;  // 상품 번호

}