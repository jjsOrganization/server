package com.jjs.ClothingInventorySaleReformPlatform.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "LIKETALLY", schema = "jjs")
public class Liketally {  // 좋아요 집계
    @Id
    @Column(name = "ID")
    private Integer id;  // 쓸모없는 컬럼

    @NotNull
    @Column(name = "PORTFOLIO_NUMBER", nullable = false)
    private Integer portfolioNumber;  // 포트폴리오 번호

    @NotNull
    @Column(name = "LIKE_TALLY", nullable = false)
    private Integer likeTally;  // 좋아요 집계

}