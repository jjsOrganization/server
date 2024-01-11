package com.jjs.ClothingInventorySaleReformPlatform.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "REQUEST", schema = "jjs")
public class Request {  // 의뢰서
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "REQUEST_NUMBER", nullable = false)
    private Integer id;  // 의뢰 번호

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "CLIENT_EMAIL", nullable = false)
    private Purchaser clientEmail;  // 의뢰자(고객) 이메일

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "DESIGNER_EMAIL", nullable = false)
    private Designer designerEmail;  // 디자이너 이메일

    // 김영한 jpa 방식으로 수정 - 3
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EMAIL")
    private Seller clientEmail2;

    @NotNull
    @Lob
    @Column(name = "REQUEST_INFO", nullable = false)
    private String requestInfo;  // 의뢰 정보

    @NotNull
    @Column(name = "REQUEST_IMG", nullable = false)
    private byte[] requestImg;  // 의뢰 사진

    @NotNull
    @Column(name = "STATUS", nullable = false)
    private Character status;  // 상태

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "PRODUCT_NUMBER", nullable = false)
    private Product productNumber;  // 상품 번호

    @OneToMany(mappedBy = "requestNumber")
    private Set<Estimate> estimates = new LinkedHashSet<>();

}