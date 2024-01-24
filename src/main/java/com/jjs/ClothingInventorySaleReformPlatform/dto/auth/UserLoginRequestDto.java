package com.jjs.ClothingInventorySaleReformPlatform.dto.auth;

import lombok.Data;

@Data
public class UserLoginRequestDto {
    private String memberId;
    private String password;
}
