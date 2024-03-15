package com.jjs.ClothingInventorySaleReformPlatform.dto.auth.updateRequest;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DesignerUpdateDTO {

    private String password;
    private String rePassword;
    private String phoneNumber;
    private String address;
}
