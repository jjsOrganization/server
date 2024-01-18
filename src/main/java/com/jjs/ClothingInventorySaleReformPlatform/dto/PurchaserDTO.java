package com.jjs.ClothingInventorySaleReformPlatform.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PurchaserDTO {

    private String email;
    private String password;
    private String name;
    private String phoneNumber;
    private String role;
    // Purchaser 특화 정보
    private String address;
}
