package com.jjs.ClothingInventorySaleReformPlatform.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AuthResultCode {

    // Member
    REGISTER_SUCCESS(200, "M001", "회원가입 되었습니다."),
    LOGIN_SUCCESS(200, "M002", "로그인 되었습니다."),
    REISSUE_SUCCESS(200, "M003", "재발급 되었습니다."),
    LOGOUT_SUCCESS(200, "M004", "로그아웃 되었습니다."),
    GET_MY_INFO_SUCCESS(200, "M005", "내 정보 조회 완료"),

    // Product
    REGISTER_PRODUCT_SUCCESS(200, "P001", "상품 등록이 완료되었습니다."),

    // Portfolio
    REGISTER_PORTFOLIO_SUCCESS(200, "F001", "포트폴리오 등록이 완료되었습니다."),
    MODIFY_PORTFOLIO_SUCCESS(300, "F002", "포트폴리오 수정이 완료되었습니다.");

    private int status;
    private final String code;
    private final String message;
}
