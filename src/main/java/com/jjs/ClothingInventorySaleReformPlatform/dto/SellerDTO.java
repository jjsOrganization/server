package com.jjs.ClothingInventorySaleReformPlatform.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SellerDTO {

    private String email;
    private String password;
    private String name;
    private String phoneNumber;
    private String role;

    // Seller 특화 정보
    private String storeName;
    private String storeAddress;
    private String businessNumber;
}
