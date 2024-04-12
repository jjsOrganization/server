package com.jjs.ClothingInventorySaleReformPlatform.domain.user.dto.updateRequest;

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
