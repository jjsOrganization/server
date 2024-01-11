package com.jjs.ClothingInventorySaleReformPlatform.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "COMPLETELIST", schema = "jjs")
public class Completelist {
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
    private String userEmail;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "EMAIL", nullable = false)
    private Designer useremail;

    @Column(name = "PRODUCT_NUMBER")
    private Integer productNumber;  // 상품 번호

    @NotNull
    @Column(name = "PRICE", nullable = false)
    private Integer price;  // 가격

    @NotNull
    @Column(name = "TRADE_DATE", nullable = false)
    private LocalDate tradeDate;  // 거래 날짜

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ESTIMATE_NUMBER")
    private Estimate estimateNumber;  // 견적서 번호

}