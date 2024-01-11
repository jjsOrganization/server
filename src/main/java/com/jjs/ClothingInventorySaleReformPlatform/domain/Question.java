package com.jjs.ClothingInventorySaleReformPlatform.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "QUESTION", schema = "jjs")
public class Question {  // 질문
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "QUESTION_NUMBER", nullable = false)
    private Integer id;  // 질문 번호

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "PURCHASER_EMAIL", nullable = false)
    private Purchaser purchaserEmail;  // 구매자 번호

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "DESIGNER_EMAIL", nullable = false)
    private Designer designerEmail;  // 디자이너 이메일

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "SELLER_EMAIL", nullable = false)
    private Seller sellerEmail;  // 판매자 이메일

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "PRODUCT_NUMBER", nullable = false)
    private Product productNumber;  // 제품 번호

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ESTIMATE_NUMBER")
    private Estimate estimateNumber;  // 견적서 번호

    @Lob
    @Column(name = "QUESTION_EXPLANATION")
    private String questionExplanation;  // 질문 내용

}