package com.jjs.ClothingInventorySaleReformPlatform.domain.cart;

import com.jjs.ClothingInventorySaleReformPlatform.common.entity.BaseEntity;
import com.jjs.ClothingInventorySaleReformPlatform.domain.PurchaserInfo;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "CART")
public class Cart {  // 장바구니

    // id(jpa는 pk 필요한 듯), 구매자 이메일, 상품 번호

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // 기본키 자동 생성 - IDENTITY : 기본키 생성을 db에 위임(원래 회원도 이걸로 권장)
    private Long id;

    private int count; // 카트에 담긴 상품 개수

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="EMAIL")
    PurchaserInfo purchaserInfo;

    @OneToMany(fetch = FetchType.EAGER)
    private List<CartProduct> cart_products = new ArrayList<>();

    @DateTimeFormat(pattern = "yyyy-mm-dd")
    private LocalDate createDate; // 날짜

    @PrePersist
    public void createDate(){
        this.createDate = LocalDate.now();
    }

    public static Cart createCart(PurchaserInfo purchaserInfo) {
        Cart cart = new Cart();
        cart.purchaserInfo = purchaserInfo;
        cart.count = 0;
        return cart;
    }

}