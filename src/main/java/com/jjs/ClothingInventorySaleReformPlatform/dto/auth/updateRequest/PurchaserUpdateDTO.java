package com.jjs.ClothingInventorySaleReformPlatform.dto.auth.updateRequest;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PurchaserUpdateDTO {

    private String password;
    private String rePassword;
    private String phoneNumber;
    private String address;
}
