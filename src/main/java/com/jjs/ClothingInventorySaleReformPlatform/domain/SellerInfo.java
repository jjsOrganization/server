package com.jjs.ClothingInventorySaleReformPlatform.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "SELLER")
public class SellerInfo {  // 판매자

    // 이메일, 비밀번호, 이름, 닉네임, 매장명, 매장주소, 사업자번호, 전화번호

    @Id
    private String email;  // User 엔티티의 email과 동일

    @Column(name = "STORE_NAME")
    private String storeName;  // 상점 이름

    @Column(name = "STORE_ADDRESS")
    private String storeAddress;  // 상점 주소


    @Column(name = "BUSINESS_NUMBER")
    private String businessNumber;  // 사업자 번호

    @OneToOne
    @MapsId  // SellerInfo 엔티티의 PK를 User 엔티티의 PK와 동일하게 매핑
    @JoinColumn(name = "EMAIL")  // User 테이블의 email을 외래 키로 사용
    private User user;  // User 엔티티에 대한 참조

//    public static SellerInfo toSeller(SellerDTO sellerDTO) {
//        SellerInfo sellerInfo = new SellerInfo();
//
//        sellerInfo.setEmail(sellerDTO.getEmail());
//        sellerInfo.setPassword(sellerDTO.getPassword());
//        sellerInfo.setNickName(sellerDTO.getNickName());
//        sellerInfo.setName(sellerDTO.getName());
//        sellerInfo.setBusinessNumber(sellerDTO.getBusinessNumber());
//        sellerInfo.setStoreAddress(sellerDTO.getStoreAddress());
//        sellerInfo.setPhoneNumber(sellerDTO.getPhoneNumber());
//
//        return sellerInfo;
//    }
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