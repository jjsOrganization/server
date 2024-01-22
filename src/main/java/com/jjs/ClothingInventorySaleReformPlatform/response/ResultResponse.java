package com.jjs.ClothingInventorySaleReformPlatform.response;

import lombok.Getter;

@Getter
public class ResultResponse {

    private int status;
    private String code;
    private String message;
    private Object data;

    public static ResultResponse of(AuthResultCode resultCode, Object data) {
        return new ResultResponse(resultCode, data);
    }

    public ResultResponse(AuthResultCode resultCode, Object data) {
        this.status = resultCode.getStatus();
        this.code = resultCode.getCode();
        this.message = resultCode.getMessage();
        this.data = data;
    }
}
