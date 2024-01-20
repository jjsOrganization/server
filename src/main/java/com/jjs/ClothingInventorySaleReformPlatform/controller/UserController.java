package com.jjs.ClothingInventorySaleReformPlatform.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jjs.ClothingInventorySaleReformPlatform.dto.*;
import com.jjs.ClothingInventorySaleReformPlatform.exception.ControllerExceptionHandler;
import com.jjs.ClothingInventorySaleReformPlatform.exception.CustomValidationException;
import com.jjs.ClothingInventorySaleReformPlatform.jwt.dto.TokenDto;
import com.jjs.ClothingInventorySaleReformPlatform.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.HashMap;
import java.util.Map;

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
    public String joinPurchaser(@Valid PurchaserDTO purchaserDTO, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            Map<String, String> errorMap = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errorMap.put(error.getField(), error.getDefaultMessage());
            }
            throw new CustomValidationException("유효성 검사 실패", errorMap);
        } else {
            System.out.println(purchaserDTO.getEmail());
            userService.joinPurchaser(purchaserDTO);

            return "ok";
        }

    }

    // 판매자 회원가입
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
