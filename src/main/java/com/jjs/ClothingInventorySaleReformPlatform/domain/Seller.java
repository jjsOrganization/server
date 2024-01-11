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
@Table(name = "SELLER", schema = "jjs")
public class Seller {  // 판매자
    @Id
    @Size(max = 30)
    @Column(name = "EMAIL", nullable = false, length = 30)
    private String email;  // 이메일

    @Size(max = 30)
    @Column(name = "STORE_NAME", length = 30)
    private String storeName;  // 매장명

    @Size(max = 30)
    @NotNull
    @Column(name = "PASSWORD", nullable = false, length = 30)
    private String password;  // 비밀번호

    @Size(max = 15)
    @NotNull
    @Column(name = "PHONENUMBER", nullable = false, length = 15)
    private String phonenumber;  // 전화번호

    @Size(max = 40)
    @Column(name = "STORE_ADDRESS", length = 40)
    private String storeAddress;  // 매장 주소



    @OneToMany(mappedBy = "userEmail")
    private Set<Completelist> completelists = new LinkedHashSet<>();

    // 김영한 jpa 방식으로 수정 - 4
    @OneToMany(mappedBy = "clientEmail2")
    private Set<Estimate> estimates = new LinkedHashSet<>();

    @OneToMany(mappedBy = "sellerEmail")
    private Set<Product> products = new LinkedHashSet<>();

    @OneToMany(mappedBy = "sellerEmail")
    private Set<Question> questions = new LinkedHashSet<>();

    // 김영한 jpa 방식으로 수정 - 3
    @OneToMany(mappedBy = "clientEmail2")
    private Set<Request> requests = new LinkedHashSet<>();


    // 김영한 jpa 방식으로 수정 - 2
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sellerEmail", nullable = false)
    private Revenuetally revenuetally;


}