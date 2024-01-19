package com.jjs.ClothingInventorySaleReformPlatform.controller;

import com.jjs.ClothingInventorySaleReformPlatform.dto.DesignerDTO;
import com.jjs.ClothingInventorySaleReformPlatform.dto.PurchaserDTO;
import com.jjs.ClothingInventorySaleReformPlatform.dto.SellerDTO;
import com.jjs.ClothingInventorySaleReformPlatform.dto.UserDTO;
import com.jjs.ClothingInventorySaleReformPlatform.exception.ControllerExceptionHandler;
import com.jjs.ClothingInventorySaleReformPlatform.exception.CustomValidationException;
import com.jjs.ClothingInventorySaleReformPlatform.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

//@Controller
@RestController
@ResponseBody
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

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
