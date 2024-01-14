package com.jjs.ClothingInventorySaleReformPlatform.controller;

import com.jjs.ClothingInventorySaleReformPlatform.domain.Seller;
import com.jjs.ClothingInventorySaleReformPlatform.dto.SellerDTO;
import com.jjs.ClothingInventorySaleReformPlatform.service.SellerService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@RequiredArgsConstructor
@Controller
public class SellerController {
    private final SellerService sellerService;


    @GetMapping("/member/seller-save")
    public String sellerSaveForm(Model model) {
        model.addAttribute("sellerDTO", new Seller()); //회원 정보를 입력받기 위해 폼(DTO) 전송
        return "member/seller_save";
    }

    @PostMapping("/member/seller-save")
    public String save(@Valid SellerDTO sellerDTO, BindingResult result) { // 이메일 입력 받았는지 유효성 검사
        if (result.hasErrors()) {
            return "member/seller_save";
        }
        sellerService.save(sellerDTO);
        return "redirect:/";
    }

    @GetMapping("/member/seller-login")
    public String loginForm() {
        return "member/seller_login";
    }

    @PostMapping("/member/seller-login")
    public String login(@Valid SellerDTO sellerDTO, HttpSession session) { // 수정 안함
        String loginEmail = sellerService.login(sellerDTO);
        if (loginEmail != null) {
            session.setAttribute("loginEmail", loginEmail);
            return "main";
        }else{
            return "member/seller-login";
        }
    }

}
