package com.jjs.ClothingInventorySaleReformPlatform.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jjs.ClothingInventorySaleReformPlatform.dto.*;
import com.jjs.ClothingInventorySaleReformPlatform.error.ErrorCode;
import com.jjs.ClothingInventorySaleReformPlatform.error.ErrorResponse;
import com.jjs.ClothingInventorySaleReformPlatform.error.exception.CustomValidationException;
import com.jjs.ClothingInventorySaleReformPlatform.jwt.dto.TokenDto;
import com.jjs.ClothingInventorySaleReformPlatform.response.ResultResponse;
import com.jjs.ClothingInventorySaleReformPlatform.service.UserService;
import com.jjs.ClothingInventorySaleReformPlatform.response.AuthResultCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

//@Controller
@RestController
@ResponseBody
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /*  // 로그인 JSON 방식 요청했을 때
    @PostMapping("/auth/login")
    public TokenDto login(@RequestBody UserLoginRequestDto memberLoginRequestDto) {
        String memberId = memberLoginRequestDto.getMemberId();
        String password = memberLoginRequestDto.getPassword();
        TokenDto tokenDto = userService.login(memberId, password);
        return tokenDto;
    }
     */

    // 로그인 JSON 방식 또는 FormData 형식 처리
    @PostMapping("/auth/login")
    public TokenDto login(HttpServletRequest request) {
        try {
            UserLoginRequestDto memberLoginRequestDto;
            String contentType = request.getContentType();

            if (contentType != null && contentType.contains("application/json")) {
                // JSON 형식의 요청 처리
                ObjectMapper mapper = new ObjectMapper();
                memberLoginRequestDto = mapper.readValue(request.getInputStream(), UserLoginRequestDto.class);
            } else {
                // FormData 형식의 요청 처리
                MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
                String memberId = multipartRequest.getParameter("memberId");
                String password = multipartRequest.getParameter("password");
                // 여기에서 필요한 경우 파일 처리도 할 수 있습니다.
                // MultipartFile file = multipartRequest.getFile("file");

                memberLoginRequestDto = new UserLoginRequestDto();
                memberLoginRequestDto.setMemberId(memberId);
                memberLoginRequestDto.setPassword(password);
            }

            // 로그인 처리
            String memberId = memberLoginRequestDto.getMemberId();
            String password = memberLoginRequestDto.getPassword();
            TokenDto tokenDto = userService.login(memberId, password);
            return tokenDto;
        } catch (Exception e) {
            throw new RuntimeException("로그인 요청 처리 중 오류가 발생했습니다.", e);
        }
    }

    /**
     * 로그인 후 accessToken 검증하는 용도 - 로그로 확인
     * (로그인 후 accessToken 복사해서 http://localhost:8080/auth/login-test 경로의 Headers의 Authorization에 Bearer+공백+accessToken(+포함x) 붙여넣고 실행)
     * 만약 accessToken일 일치하지 않으면 JWT signature does not match locally computed signature. JWT validity cannot be asserted and should not be trusted.
     * 일치하지 않는다는 에러 발생
      */
    @PostMapping("/auth/login-test")
    public String test() {
        return "success";
    }

    // 구매자 회원가입
    @PostMapping("/auth/join-purchaser")
    public ResponseEntity<Object> joinPurchaser(@Valid @RequestBody PurchaserDTO purchaserDTO, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            List<ErrorResponse.FieldError> fieldErrors = bindingResult.getFieldErrors().stream()
                    .map(fieldError -> new ErrorResponse.FieldError(
                            fieldError.getField(),
                            fieldError.getRejectedValue() == null ? "" : fieldError.getRejectedValue().toString(),
                            fieldError.getDefaultMessage()))
                    .collect(Collectors.toList());

            ErrorResponse errorResponse = new ErrorResponse(ErrorCode.INVALID_INPUT_VALUE, fieldErrors);
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        userService.joinPurchaser(purchaserDTO);
        ResultResponse resultResponse = ResultResponse.of(AuthResultCode.REGISTER_SUCCESS, Collections.singletonMap("email", purchaserDTO.getEmail()));
        return new ResponseEntity<>(resultResponse, HttpStatus.OK);
    }

    // 판매자 회원가입
    @PostMapping("/auth/join-seller")
    public ResponseEntity<Object> joinSeller(@Valid @RequestBody SellerDTO sellerDTO, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            List<ErrorResponse.FieldError> fieldErrors = bindingResult.getFieldErrors().stream()
                    .map(fieldError -> new ErrorResponse.FieldError(
                            fieldError.getField(),
                            fieldError.getRejectedValue() == null ? "" : fieldError.getRejectedValue().toString(),
                            fieldError.getDefaultMessage()))
                    .collect(Collectors.toList());

            ErrorResponse errorResponse = new ErrorResponse(ErrorCode.INVALID_INPUT_VALUE, fieldErrors);
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        userService.joinSeller(sellerDTO);
        ResultResponse resultResponse = ResultResponse.of(AuthResultCode.REGISTER_SUCCESS, Collections.singletonMap("email", sellerDTO.getEmail()));
        return new ResponseEntity<>(resultResponse, HttpStatus.OK);
    }

    // 디자이너 회원가입
    @PostMapping("/auth/join-designer")
    public ResponseEntity<Object> joinDesigner(@Valid @RequestBody DesignerDTO designerDTO, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            List<ErrorResponse.FieldError> fieldErrors = bindingResult.getFieldErrors().stream()
                    .map(fieldError -> new ErrorResponse.FieldError(
                            fieldError.getField(),
                            fieldError.getRejectedValue() == null ? "" : fieldError.getRejectedValue().toString(),
                            fieldError.getDefaultMessage()))
                    .collect(Collectors.toList());

            ErrorResponse errorResponse = new ErrorResponse(ErrorCode.INVALID_INPUT_VALUE, fieldErrors);
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        userService.joinDesigner(designerDTO);
        ResultResponse resultResponse = ResultResponse.of(AuthResultCode.REGISTER_SUCCESS, Collections.singletonMap("email", designerDTO.getEmail()));
        return new ResponseEntity<>(resultResponse, HttpStatus.OK);

    }

}
