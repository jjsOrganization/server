package com.jjs.ClothingInventorySaleReformPlatform.domain.order.entity;

import com.jjs.ClothingInventorySaleReformPlatform.domain.product.entity.Product;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Table(name = "ORDER_DETAIL")
@Setter
public class OrderDetail {

    @Id
    @Column(name = "order_detail_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderDetailId;  // 주문 상세 번호

    /**
     * 상품 번호를 참조하는 외래 키
     */
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;  // 상품 번호

    /**
     * 주문 번호를 참조하는 외래 키
     */
    @ManyToOne
    @JoinColumn(name = "order_Id")
    private Order order;  // 주문 번호

    private int quantity;  // 주문 수량
    private int price;  // 주문 금액

}
