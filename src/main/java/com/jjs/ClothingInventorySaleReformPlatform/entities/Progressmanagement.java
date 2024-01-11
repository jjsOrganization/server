package com.jjs.ClothingInventorySaleReformPlatform.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "PROGRESSMANAGEMENT", schema = "jjs")
public class Progressmanagement {  // 형상 관리
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PROGRESS_NUMBER", nullable = false)
    private Integer id;  // 형상 번호

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ESTIMATE_NUMBER", nullable = false)
    private Estimate estimateNumber;  // 견적서 번호

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "DESIGNER_EMAIL", nullable = false)
    private Designer designerEmail;  // 디자이너 이메일

    @NotNull
    @Lob
    @Column(name = "PROGRESS_INFO", nullable = false)
    private String progressInfo;  // 형상 정보

    @NotNull
    @Column(name = "PROGRESS_IMG", nullable = false)
    private byte[] progressImg;  // 형상 이미지

}