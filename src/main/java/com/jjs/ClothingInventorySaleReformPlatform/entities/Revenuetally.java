package com.jjs.ClothingInventorySaleReformPlatform.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "REVENUETALLY", schema = "jjs")
public class Revenuetally {  // 수익 집계
    @Id
    @Size(max = 30)
    @Column(name = "SELLER_EMAIL", nullable = false, length = 30)
    private String sellerEmail;  // 판매자 이메일

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "SELLER_EMAIL", nullable = false)
    private Seller seller;

    @NotNull
    @Column(name = "TOTAL_REVENUE", nullable = false)
    private Integer totalRevenue;  // 총 수입

}