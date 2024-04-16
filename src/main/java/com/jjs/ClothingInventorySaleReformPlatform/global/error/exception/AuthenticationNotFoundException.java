package com.jjs.ClothingInventorySaleReformPlatform.global.error.exception;

import com.jjs.ClothingInventorySaleReformPlatform.global.error.ErrorCode;

public class AuthenticationNotFoundException extends BusinessException{

    public AuthenticationNotFoundException() {
        super(ErrorCode.AUTHENTICATION_NOT_FOUND);
    }
}
