package com.jjs.ClothingInventorySaleReformPlatform.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "PORTFOLIO", schema = "jjs")
public class Portfolio {  // 포트폴리오
    @Id
    @Column(name = "PORTFOLIO_NUMBER", nullable = false)
    private Integer id;  // 포트폴리오 번호

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "DESINGER_EMAIL", nullable = false)
    private Designer desingerEmail;  // 디자이너 이메일

    @NotNull
    @Lob
    @Column(name = "EXPLANATION", nullable = false)
    private String explanation;  // 설명

    @NotNull
    @Column(name = "PREVIOUS_RESULTS_IMG", nullable = false)
    private byte[] previousResultsImg;  // 이전 결과 사진

    @NotNull
    @Lob
    @Column(name = "PREVIOUS_RESULTS_EXPLANATION", nullable = false)
    private String previousResultsExplanation;  // 이전 결과 설명

}