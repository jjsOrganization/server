package com.jjs.ClothingInventorySaleReformPlatform.controller.auth;

import com.jjs.ClothingInventorySaleReformPlatform.domain.user.PurchaserInfo;
import com.jjs.ClothingInventorySaleReformPlatform.dto.auth.DesignerDTO;
import com.jjs.ClothingInventorySaleReformPlatform.dto.auth.PurchaserDTO;
import com.jjs.ClothingInventorySaleReformPlatform.dto.auth.SellerDTO;
import com.jjs.ClothingInventorySaleReformPlatform.dto.auth.UserLoginRequestDto;
import com.jjs.ClothingInventorySaleReformPlatform.error.ErrorCode;
import com.jjs.ClothingInventorySaleReformPlatform.error.ErrorResponse;
import com.jjs.ClothingInventorySaleReformPlatform.jwt.dto.TokenDto;
import com.jjs.ClothingInventorySaleReformPlatform.jwt.provider.JwtTokenProvider;
import com.jjs.ClothingInventorySaleReformPlatform.response.AuthResultCode;
import com.jjs.ClothingInventorySaleReformPlatform.response.ResultResponse;
import com.jjs.ClothingInventorySaleReformPlatform.service.auth.CustomUserDetailsService;
import com.jjs.ClothingInventorySaleReformPlatform.service.auth.UserService;
import com.jjs.ClothingInventorySaleReformPlatform.service.cart.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

//@Controller
@RestController
@ResponseBody
@RequiredArgsConstructor
@Tag(name = "회원가입 및 로그인 컨트롤러", description = "로그인, 로그인 테스트, 회원가입 API")
public class UserController {

    private final UserService userService;
    private final CartService cartService;
    private final CustomUserDetailsService customUserDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;


    @PostMapping("/auth/login")
    @Operation(summary = "로그인", description = "구매자/판매자/디자이너 로그인을 합니다.")
    public ResponseEntity<Object> login(@RequestBody UserLoginRequestDto memberLoginRequestDto) {
        try {
            String memberId = memberLoginRequestDto.getMemberId();
            String password = memberLoginRequestDto.getPassword();
            TokenDto tokenDto = userService.login(memberId, password);

            ResultResponse resultResponse = ResultResponse.of(
                    AuthResultCode.LOGIN_SUCCESS, // 로그인 성공 코드
                    tokenDto // 로그인 성공 시 반환되는 토큰 정보
            );
            return new ResponseEntity<>(resultResponse, HttpStatus.OK);
        } catch (AuthenticationException e) {
            ErrorResponse errorResponse = ErrorResponse.of(
                    ErrorCode.AUTHENTICATION_NOT_FOUND, // 로그인 실패 코드
                    e.getMessage() // 실패 메시지
            );
            return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * 로그인 후 accessToken 검증하는 용도 - 로그로 확인
     * (로그인 후 accessToken 복사해서 http://localhost:8080/auth/login-test 경로의 Headers의 Authorization에 accessToken 붙여넣고 실행)
     * 만약 accessToken일 일치하지 않으면 JWT signature does not match locally computed signature. JWT validity cannot be asserted and should not be trusted.
     * 일치하지 않는다는 에러 발생
      */
    @PostMapping("/auth/login-test")
    public ResponseEntity<Object> test(@RequestHeader(value = "Authorization", required = false) String tokenHeader) {
        if (tokenHeader != null && jwtTokenProvider.validateToken(tokenHeader)) {
            // 토큰 검증 성공
            String successMessage = "success";
            ResultResponse successResponse = ResultResponse.of(
                    AuthResultCode.LOGIN_SUCCESS, // 접근 성공 코드
                    successMessage // 성공 메시지
            );
            return ResponseEntity.ok(successResponse);
        } else {
            // 토큰 검증 실패 또는 토큰 누락
            String failMessage = "Access Denied: Invalid or missing token.";
            ErrorResponse failResponse = ErrorResponse.of(
                    ErrorCode.AUTHENTICATION_NOT_FOUND, // 접근 실패 코드
                    failMessage // 실패 메시지
            );
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(failResponse);
        }
    }

    // 구매자 회원가입
    @PostMapping("/auth/join-purchaser")
    @Operation(summary = "구매자 회원가입", description = "구매자 회원가입을 합니다.")
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

        // 구매자는 회원가입과 동시에 장바구니 생성
        PurchaserInfo purchaserInfo = new PurchaserInfo();
        purchaserInfo.setEmail(purchaserDTO.getEmail());
        // 장바구니 생성
        cartService.createCart(purchaserInfo);

        ResultResponse resultResponse = ResultResponse.of(AuthResultCode.REGISTER_SUCCESS, Collections.singletonMap("email", purchaserDTO.getEmail()));
        return new ResponseEntity<>(resultResponse, HttpStatus.OK);
    }

    // 판매자 회원가입
    @PostMapping("/auth/join-seller")
    @Operation(summary = "판매자 회원가입", description = "판매자 회원가입을 합니다.")
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
    @Operation(summary = "디자이너 회원가입", description = "디자이너 회원가입을 합니다.")
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
