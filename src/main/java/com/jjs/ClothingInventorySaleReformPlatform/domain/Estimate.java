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
@Table(name = "ESTIMATE", schema = "jjs")
public class Estimate {  // 견적서
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ESTIMATE_NUMBER", nullable = false)
    private Integer id;  // 견적서 번호

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "REQUEST_NUMBER", nullable = false)
    private Request requestNumber;  // 의뢰 번호

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "CLIENT_EMAIL", nullable = false)
    private Purchaser clientEmail;  // 의뢰자 이메일

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "DESIGNER_EMAIL", nullable = false)
    private Designer designerEmail;  // 디자이너 이메일

    @NotNull
    @Lob
    @Column(name = "ESTIMATE_INFO", nullable = false)
    private String estimateInfo;  // 견적서 정보

    @NotNull
    @Column(name = "ESTIMATE_IMG", nullable = false)
    private byte[] estimateImg;  // 견적서 사진

    @NotNull
    @Column(name = "PRICE", nullable = false)
    private Integer price;  // 가격

    @OneToMany(mappedBy = "estimateNumber")
    private Set<Completelist> completelists = new LinkedHashSet<>();

    @OneToMany(mappedBy = "estimateNumber")
    private Set<Progressmanagement> progressmanagements = new LinkedHashSet<>();

    @OneToMany(mappedBy = "estimateNumber")
    private Set<Question> questions = new LinkedHashSet<>();

}