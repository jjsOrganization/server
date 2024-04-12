package com.jjs.ClothingInventorySaleReformPlatform.domain.cart.entity;

import com.jjs.ClothingInventorySaleReformPlatform.domain.user.entity.PurchaserInfo;
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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // 기본키 자동 생성 - IDENTITY : 기본키 생성을 db에 위임(원래 회원도 이걸로 권장)
    private Long id;

    private int count; // 카트에 담긴 상품 개수

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="EMAIL")
    PurchaserInfo purchaserInfo;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
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