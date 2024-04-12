package com.jjs.ClothingInventorySaleReformPlatform.domain.reform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "ProgressManagement")
public class Progressmanagement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PROGRESS_NUMBER", nullable = false)
    private Long id;

    @NotNull
    @Column(length = 30)
    private String clientEmail;  // 의뢰자 이메일

    @NotNull
    @Column(length = 30)
    private String designerEmail;  // 디자이너 이메일

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "REQUEST_NUMBER", nullable = false)
    private ReformRequest requestNumber;  // 의뢰 번호

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ESTIMATE_NUMBER", nullable = false)
    private Estimate estimateNumber;  // 견적서 번호

    private String productImgUrl;  // 리폼 전 사진 - 상품 사진
    private String firstImgUrl;  // 리폼 중 사진 1
    private String secondImgUrl;  // 리폼 중 사진 2
    private String completeImgUrl;  // 리폼 완료 사진

    /**
     * 구매자가 견적서 수락 시, REFORM_START 저장
     * 디자이너가 firstImgUrl 등록 시, REFORMING으로 변경
     * 디자이너가 completeImgUrl 등록 시, REFORM_COMPLETE으로 변경
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private ProgressStatus progressStatus;  // 리폼 상태
}

