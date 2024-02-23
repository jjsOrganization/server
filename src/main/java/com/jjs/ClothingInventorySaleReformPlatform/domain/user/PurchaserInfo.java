package com.jjs.ClothingInventorySaleReformPlatform.domain.user;

import com.jjs.ClothingInventorySaleReformPlatform.domain.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "PURCHASER")
public class PurchaserInfo {

    @Id
//    @Column(name = "PURCHASER_EMAIL", nullable = false)
    private String email;  // User 엔티티의 email과 동일

    @Column(name = "ADDRESS", nullable = false)
    private String address;  // 구매자 주소

    @OneToOne
    @MapsId  // PurchaseInfo 엔티티의 PK를 User 엔티티의 PK와 동일하게 매핑
    @JoinColumn(name = "EMAIL")  // User 테이블의 email을 외래 키로 사용
    private User user;  // User 엔티티에 대한 참조


}

