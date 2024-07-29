package com.jjs.ClothingInventorySaleReformPlatform.domain.order.dto;

import com.jjs.ClothingInventorySaleReformPlatform.domain.order.entity.Order;
import com.jjs.ClothingInventorySaleReformPlatform.domain.order.entity.OrderDetail;
import com.jjs.ClothingInventorySaleReformPlatform.domain.product.entity.Product;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderDetailDTO {
    private Long productId;
    private int quantity;

    public OrderDetail toEntity(Order order, Product product) {
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setOrder(order);
        orderDetail.setProduct(product);
        orderDetail.setQuantity(this.quantity);
        orderDetail.setPrice(product.getPrice() * this.quantity);
        return orderDetail;
    }
}
