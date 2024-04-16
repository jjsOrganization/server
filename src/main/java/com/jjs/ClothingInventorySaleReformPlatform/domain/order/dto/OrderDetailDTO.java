package com.jjs.ClothingInventorySaleReformPlatform.domain.order.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderDetailDTO {
    private Long productId;
    private int quantity;
}
