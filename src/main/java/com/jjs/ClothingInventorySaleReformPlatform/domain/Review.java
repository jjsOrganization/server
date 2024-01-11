package com.jjs.ClothingInventorySaleReformPlatform.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "REVIEW", schema = "jjs")
public class Review {  // 후기
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "REVIEW_NUMBER", nullable = false)
    private Integer id;  // 후기 번호

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "PRODUCT_NUMBER", nullable = false)
    private Product productNumber;  // 상품 번호

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "PURCHASER_EMAIL", nullable = false)
    private Purchaser purchaserEmail; // 구매자 이메일

    @NotNull
    @Lob
    @Column(name = "REVIEW_EXPLANATION", nullable = false)
    private String reviewExplanation;  // 후기 내용

}