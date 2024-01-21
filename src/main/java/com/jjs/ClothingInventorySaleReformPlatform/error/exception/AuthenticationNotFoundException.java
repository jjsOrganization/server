package com.jjs.ClothingInventorySaleReformPlatform.error.exception;

import com.jjs.ClothingInventorySaleReformPlatform.error.ErrorCode;

public class AuthenticationNotFoundException extends BusinessException{

    public AuthenticationNotFoundException() {
        super(ErrorCode.AUTHENTICATION_NOT_FOUND);
    }
}
