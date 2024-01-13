package com.jjs.ClothingInventorySaleReformPlatform.controller;

import com.jjs.ClothingInventorySaleReformPlatform.domain.Purchaser;
import com.jjs.ClothingInventorySaleReformPlatform.dto.PurchaserDTO;
import com.jjs.ClothingInventorySaleReformPlatform.service.PurchaserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseBody
public class PurchaserController {

    private final PurchaserService purchaserService;

    public PurchaserController(PurchaserService purchaserService) {
        this.purchaserService = purchaserService;
    }

    @PostMapping("/join")
    public String joinProcess(PurchaserDTO purchaserDTO) {
        System.out.println(purchaserDTO.getEmail());
        purchaserService.joinProcess(purchaserDTO);

        return "ok";
    }




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
