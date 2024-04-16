package com.jjs.ClothingInventorySaleReformPlatform.domain.reform.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReformOrderRequestDTO {

    private String postcode;
    private String address;
    private String detailAddress;
    private String phoneNumber;
    private String deliveryRequest;
}
