package com.jjs.ClothingInventorySaleReformPlatform.dto.reform.estimate.request;

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
