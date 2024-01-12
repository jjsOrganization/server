package com.jjs.ClothingInventorySaleReformPlatform.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "COMPLETELIST")
public class Completelist {  // 완료 목록

    // 사용자 이메일, 상품번호, 가격, 거래날짜, 견적서 번호
    @Id
    @Column(name = "ID")
    private Integer id;  // 쓸모없는 컬럼

    /*
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "EMAIL", nullable = false)
    private Designer userEmail2;  // 사용자(누구든 상관 없는거 같은데?) 이메일
    */

    @NotNull
    @Column(name = "USER_EMAIL")
    private String userEmail;  // 사용자 이메일

    @Column(name = "PRODUCT_NUMBER")
    private Integer productNumber;  // 상품 번호

    @NotNull
    @Column(name = "PRICE", nullable = false)
    private Integer price;  // 가격

    @NotNull
    @Column(name = "TRADE_DATE", nullable = false)
    private LocalDate tradeDate;  // 거래 날짜

    @NotNull
    @Column(name = "ESTIMATE_NUMBER", nullable = false)
    private Integer estimateNumber;

/*
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ESTIMATE_NUMBER")
    private Estimate estimateNumber;  // 견적서 번호

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "EMAIL", nullable = false)
    private Designer useremail;
*/
}