package com.jjs.ClothingInventorySaleReformPlatform.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "PRODUCT", schema = "jjs")
public class Product {  // 상품
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PRODUCT_NUMBER", nullable = false)
    private Integer id;  // 상품 번호

    @Size(max = 30)
    @NotNull
    @Column(name = "PRODUCT_NAME", nullable = false, length = 30)
    private String productName;  // 상품명

    @NotNull
    @Column(name = "PRICE", nullable = false)
    private Integer price;  // 가격

    @NotNull
    @Column(name = "PRODUCT_IMG", nullable = false)
    private byte[] productImg;  // 상품 사진

    @NotNull
    @Lob
    @Column(name = "PRODUCT_INFO", nullable = false)
    private String productInfo;  // 상품 정보

    @NotNull
    @Column(name = "STOCK_NUMBER", nullable = false)
    private Integer stockNumber;  // 재고수

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "SELLER_EMAIL", nullable = false)
    private Seller sellerEmail;  // 판매자 이메일

    @Size(max = 30)
    @NotNull
    @Column(name = "CATEGORY_NAME", nullable = false, length = 30)
    private String categoryName;  // 카테고리명


    @OneToMany(mappedBy = "productNumber")
    private Set<Purchaselist> purchaselists = new LinkedHashSet<>();

    @OneToMany(mappedBy = "productNumber")
    private Set<Question> questions = new LinkedHashSet<>();

    @OneToMany(mappedBy = "productNumber")
    private Set<Request> requests = new LinkedHashSet<>();

    @OneToMany(mappedBy = "productNumber")
    private Set<Review> reviews = new LinkedHashSet<>();

}