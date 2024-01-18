package com.jjs.ClothingInventorySaleReformPlatform.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "USER")
public class User {  // 구매자
    @Id
    @Column(name = "EMAIL", unique = true)  // unique = true : 유일값(중복x)
    private String email;  // 이메일

    @Column(name = "PASSWORD")
    private String password;  // 비밀번호

    @Column(name = "NAME")
    private String name;  // 이름

    @Column(name = "PHONENUMBER", unique = true)
    private String phoneNumber;  // 전화번호

    @Column(name = "ROLE")
    private String role;





    /*
    public static Purchaser toPurchaser(PurchaserDTO purchaserDTO) {
        Purchaser purchaser = new Purchaser();

        purchaser.setEmail(purchaserDTO.getEmail());
        purchaser.setPassword(purchaserDTO.getPassword());
        purchaser.setNickname(purchaserDTO.getNickname());
        purchaser.setName(purchaserDTO.getName());
        purchaser.setAddress(purchaserDTO.getAddress());
        purchaser.setPhoneNumber(purchaserDTO.getPhonenumber());

        return purchaser;
    }

     */

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