package com.jjs.ClothingInventorySaleReformPlatform.domain.user;

import jakarta.persistence.*;
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

    @Column(name = "STORE_NAME", nullable = false)
    private String storeName;  // 상점 이름

    @Column(name = "STORE_ADDRESS", nullable = false)
    private String storeAddress;  // 상점 주소


    @Column(name = "BUSINESS_NUMBER", nullable = false)
    private String businessNumber;  // 사업자 번호

    @OneToOne
    @MapsId  // SellerInfo 엔티티의 PK를 User 엔티티의 PK와 동일하게 매핑
    @JoinColumn(name = "EMAIL")  // User 테이블의 email을 외래 키로 사용
    private User user;  // User 엔티티에 대한 참조


}