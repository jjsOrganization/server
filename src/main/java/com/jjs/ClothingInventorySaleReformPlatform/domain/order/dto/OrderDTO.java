package com.jjs.ClothingInventorySaleReformPlatform.domain.order.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderDTO {

    private List<OrderDetailDTO> orderDetails;
    // 두 번째 단계에서 사용될 필드들
    private String postcode;
    private String address;
    private String detailAddress;
    private String phoneNumber;
    private String deliveryRequest;
}
