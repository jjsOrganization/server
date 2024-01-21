package com.jjs.ClothingInventorySaleReformPlatform.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jjs.ClothingInventorySaleReformPlatform.dto.*;
import com.jjs.ClothingInventorySaleReformPlatform.error.ErrorCode;
import com.jjs.ClothingInventorySaleReformPlatform.error.ErrorResponse;
import com.jjs.ClothingInventorySaleReformPlatform.error.exception.BusinessException;
import com.jjs.ClothingInventorySaleReformPlatform.exception.CustomValidationException;
import com.jjs.ClothingInventorySaleReformPlatform.jwt.dto.TokenDto;
import com.jjs.ClothingInventorySaleReformPlatform.service.UserService;
import com.jjs.ClothingInventorySaleReformPlatform.dto.AuthResultCode;
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

    /**
     * 구매자, 판매자, 디자이너 회원가입
     * 프론트 측에서 회원가입을 json 형식으로 요청을 한다면 @Valid 뒤에 @RequestBody를 붙여줘야됨
     * 현재는 json으로 요청을 받음
     */
    // 구매자 회원가입
    @ResponseBody
    @PostMapping("/auth/join-purchaser")
    public PurchaserDTO joinPurchaser(@Valid @RequestBody PurchaserDTO purchaserDTO, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            Map<String, String> errorMap = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errorMap.put(error.getField(), error.getDefaultMessage());
            }
            throw new CustomValidationException("유효성 검사 실패", errorMap);
        } else {
            System.out.println(purchaserDTO.getEmail());
            userService.joinPurchaser(purchaserDTO);

            return purchaserDTO;
            /*
            {
    "email": "Test9@test.com",
    "password": "testtest9",
    "name": "test9",
    "phoneNumber": "01011111139",
    "role": null,
    "address": "test9"
}
             */
        }

    }

    // 판매자 회원가입
    /*
    @PostMapping("/auth/join-seller")
    public String joinSeller(@Valid SellerDTO sellerDTO, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            Map<String, String> errorMap = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errorMap.put(error.getField(), error.getDefaultMessage());
            }
            throw new CustomValidationException("유효성 검사 실패", errorMap);
        } else {
            System.out.println(sellerDTO.getEmail());
            userService.joinSeller(sellerDTO);

            return "ok";
        }
    }
     */

    // 최종 로직 !!!!!!!!!!!!!1
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
        //return ResponseEntity.ok().build();
        ResultResponse resultResponse = ResultResponse.of(AuthResultCode.REGISTER_SUCCESS, Collections.singletonMap("email", sellerDTO.getEmail()));
        return new ResponseEntity<>(resultResponse, HttpStatus.OK);

    //public String joinSeller(@Valid SellerDTO sellerDTO, BindingResult bindingResult) {
/*
        if (bindingResult.hasErrors()) {
            Map<String, String> errorMap = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errorMap.put(error.getField(), error.getDefaultMessage());
            }
            AuthResponseDTO authResponseDTO = userService.joinSeller(sellerDTO);
            ErrorResponse result = ErrorResponse.of(ErrorCode.USER_EMAIL_ALREADY_EXISTS, bindingResult);
            //throw new CustomValidationException("유효성 검사 실패", errorMap);
            return new ResponseEntity<>(result, );
        } else {
            System.out.println(sellerDTO.getEmail());
            //userService.joinSeller(sellerDTO);
            AuthResponseDTO authResponseDTO = userService.joinSeller(sellerDTO);
            ResultResponse result = ResultResponse.of(AuthResultCode.REGISTER_SUCCESS, authResponseDTO);
            return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatus()));
            //return new ResponseEntity<>(sellerDTO, HttpStatus.OK);
        }

        //AuthResponseDTO authResponseDTO = userService.joinSeller(sellerDTO);
        //ResultResponse result = ResultResponse.of(AuthResultCode.REGISTER_SUCCESS, authResponseDTO);

 */

    }








    // 디자이너 회원가입
    @PostMapping("/auth/join-designer")
    public String joinDesigner(@Valid DesignerDTO designerDTO, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            Map<String, String> errorMap = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errorMap.put(error.getField(), error.getDefaultMessage());
            }
            throw new CustomValidationException("유효성 검사 실패", errorMap);
        } else {
            System.out.println(designerDTO.getEmail());
            userService.joinDesigner(designerDTO);

            return "ok";
        }

    }

    /*
    @PostMapping("/join")
    public String joinProcess(UserDTO userDTO) {
        System.out.println(userDTO.getEmail());
        userService.joinProcess(userDTO);

        return "ok";
    }
     */

    /*
    @GetMapping("/member/purchaser-save")
    public String purchaserSaveForm(Model model) {
        model.addAttribute("purchaserDTO", new Purchaser()); //회원 정보를 입력받기 위해 폼(DTO) 전송
        return "member/purchaser_save";
    }

    @PostMapping("/member/purchaser-save")
    public String save(@Valid PurchaserDTO purchaserDTO, BindingResult result) { // 이메일 입력 받았는지 유효성 검사
        if (result.hasErrors()) {
            return "member/purchaser_save";
        }
        purchaserService.save(purchaserDTO);
        return "redirect:/";
    }

    @GetMapping("/member/purchaser-login")
    public String loginForm() {
        return "member/purchaser_login";
    }

    @PostMapping("/member/purchaser-login")
    public String login(@Valid PurchaserDTO purchaserDTO, HttpSession session) { // 수정 안함
        String loginEmail = purchaserService.login(purchaserDTO);
        if (loginEmail != null) {
            session.setAttribute("loginEmail", loginEmail);
            return "main";
        }else{
            return "member/purchaser-login";
        }
    }

     */
}
