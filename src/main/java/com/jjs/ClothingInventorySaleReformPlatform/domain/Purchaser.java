package com.jjs.ClothingInventorySaleReformPlatform.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Entity
@Table(name = "PURCHASER")
public class Purchaser {  // 구매자
    @Id
    @Size(max = 30)
    @Column(name = "EMAIL", nullable = false, length = 30, unique = true)  // unique = true : 유일값(중복x)
    private String email;  // 이메일

    @Size(max = 30)
    @NotNull
    @Column(name = "PASSWORD", nullable = false, length = 30)
    private String password;  // 비밀번호

    @Size(max = 15)
    @Column(name = "NICKNAME", length = 15, unique = true)
    private String nickname;  // 닉네임

    @Size(max = 15)
    @Column(name = "NAME", length = 15)
    private String name;  // 이름

    @Size(max = 40)
    @NotNull
    @Column(name = "ADDRESS", nullable = false, length = 40)
    private String address;  // 주소

    @Size(max = 15)
    @NotNull
    @Column(name = "PHONENUMBER", nullable = false, length = 15, unique = true)
    private String phonenumber;  // 전화번호


/*
    @OneToMany(mappedBy = "clientEmail")
    private Set<Estimate> estimates = new LinkedHashSet<>();

    @OneToMany(mappedBy = "sellerEmail")
    private Set<Purchaselist> purchaselists = new LinkedHashSet<>();

    @OneToMany(mappedBy = "purchaserEmail")
    private Set<Question> questions = new LinkedHashSet<>();

    @OneToMany(mappedBy = "clientEmail")
    private Set<Request> requests = new LinkedHashSet<>();

    @OneToMany(mappedBy = "purchaserEmail")
    private Set<Review> reviews = new LinkedHashSet<>();
*/
}