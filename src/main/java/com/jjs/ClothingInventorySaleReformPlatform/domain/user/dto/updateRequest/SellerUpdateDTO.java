package com.jjs.ClothingInventorySaleReformPlatform.domain.user.dto.updateRequest;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SellerUpdateDTO {

    private String password;
    private String rePassword;
    private String phoneNumber;
    private String storeName;
    private String storeAddress;
    private String businessNumber;

}
