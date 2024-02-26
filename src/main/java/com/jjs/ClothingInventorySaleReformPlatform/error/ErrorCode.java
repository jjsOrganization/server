package com.jjs.ClothingInventorySaleReformPlatform.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    /**
     * Enum 타입으로 에러 코드 관리
     * 에러 코드가 도메인 전체적으로 흩어져 있을 경우, 코드 및 메시지의 중복이 발생하기 때문에 이를 해결하기 위한 가장 효율적인 방법
     */

    // Common
    INTERNAL_SERVER_ERROR(500, "C001", "internal server error"),
    INVALID_INPUT_VALUE(400, "C002", "invalid input type"),
    METHOD_NOT_ALLOWED(405, "C003", "method not allowed"),
    INVALID_TYPE_VALUE(400, "C004", "invalid type value"),
    BAD_CREDENTIALS(400, "C005", "bad credentials"),

    // Member
    MEMBER_NOT_EXIST(404, "M001", "member not exist"),
    USER_EMAIL_ALREADY_EXISTS(400, "M002", "user email already exists"),
    USER_PHONENUMBER_ALREADY_EXISTS(400, "M007", "user phoneNumber already exists"),
    NO_AUTHORITY(403, "M003", "no authority"),
    NEED_LOGIN(401, "M004", "need login"),
    AUTHENTICATION_NOT_FOUND(401, "M005", "Security Context에 인증 정보가 없습니다."),
    MEMBER_ALREADY_LOGOUT(400, "M006", "member already logout"),
    PASSWORD_NOT_MATCH(400, "M007", "비밀번호가 일치하지 않습니다."),

    // Auth
    REFRESH_TOKEN_INVALID(400, "A001", "refresh token invalid"),

    // Product
    INVALID_BAD_REQUEST(400, "P001", "bad request, 유효성 검사 실패"),
    IMAGE_EMPTY(400, "P002", "bad request, 첫번째 상품 이미지는 필수 입력 값 입니다");

    private int status;
    private final String code;
    private final String message;
}
