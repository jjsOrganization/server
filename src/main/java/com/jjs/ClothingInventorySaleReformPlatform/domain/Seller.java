package com.jjs.ClothingInventorySaleReformPlatform.domain;

import com.jjs.ClothingInventorySaleReformPlatform.dto.SellerDTO;
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
@Table(name = "SELLER")
public class Seller {  // 판매자

    // 이메일, 비밀번호, 이름, 닉네임, 매장명, 매장주소, 사업자번호, 전화번호

    @Id
    @Size(max = 30)
    @Column(name = "EMAIL", nullable = false, length = 30, unique = true)
    private String email;  // 이메일

    @Size(max = 30)
    @NotNull
    @Column(name = "PASSWORD", nullable = false, length = 30)
    private String password;  // 비밀번호

    @Size(max = 15)
    @Column(name = "NAME", length = 15)
    private String name;  // 이름

    @Size(max = 15)
    @Column(name = "NICKNAME", length = 15, unique = true)
    private String nickName;  // 닉네임

    @Size(max = 30)
    @Column(name = "STORE_NAME", length = 30)
    private String storeName;  // 매장명

    @Size(max = 40)
    @Column(name = "STORE_ADDRESS", length = 40)
    private String storeAddress;  // 매장 주소 (집주소 필요 없음)

    @Size(max = 10)
    @Column(name = "BUSINESSNUMBER", length = 10)
    private String businessNumber;  // 사업자 번호

    @Size(max = 15)
    @NotNull
    @Column(name = "PHONENUMBER", nullable = false, length = 15, unique = true)
    private String phoneNumber;  // 전화번호

    public static Seller toSeller(SellerDTO sellerDTO) {
        Seller seller = new Seller();

        seller.setEmail(sellerDTO.getEmail());
        seller.setPassword(sellerDTO.getPassword());
        seller.setNickName(sellerDTO.getNickName());
        seller.setName(sellerDTO.getName());
        seller.setBusinessNumber(sellerDTO.getBusinessNumber());
        seller.setStoreAddress(sellerDTO.getStoreAddress());
        seller.setPhoneNumber(sellerDTO.getPhoneNumber());

        return seller;
    }
/*
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
*/

}