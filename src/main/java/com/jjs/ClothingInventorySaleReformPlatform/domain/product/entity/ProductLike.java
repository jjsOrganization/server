package com.jjs.ClothingInventorySaleReformPlatform.domain.product.entity;

import com.jjs.ClothingInventorySaleReformPlatform.domain.user.entity.PurchaserInfo;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "PRODUCT_LIKE")
public class ProductLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchaser_email")
    private PurchaserInfo purchaser;  // 좋아요를 누른 구매자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;  // 좋아요를 받은 상품

    @Column(name = "liked_at")
    private LocalDateTime likedAt = LocalDateTime.now();  // 좋아요를 누른 시간
}
