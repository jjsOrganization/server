package com.jjs.ClothingInventorySaleReformPlatform.controller;

import com.jjs.ClothingInventorySaleReformPlatform.domain.Purchaser;
import com.jjs.ClothingInventorySaleReformPlatform.dto.DesignerDTO;
import com.jjs.ClothingInventorySaleReformPlatform.service.DesignerService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class DesignerController {

    private final DesignerService designerService;
    @GetMapping("/member/designer-save")
    public String designerSaveForm(Model model) {
        model.addAttribute("designerDTO", new DesignerDTO()); //회원 정보를 입력받기 위해 폼(DTO) 전송
        return "member/designer_save";
    }

    @PostMapping("/member/designer-save")
    public String save(@Valid DesignerDTO designerDTO, BindingResult result) { // 이메일 입력 받았는지 유효성 검사
        if (result.hasErrors()) {
            return "member/designer_save";
        }
        designerService.save(designerDTO);
        return "redirect:/";
    }

    @GetMapping("/member/designer-login")
    public String loginForm() {
        return "member/designer_login";
    }

    @PostMapping("/member/designer-login")
    public String login(@Valid DesignerDTO designerDTO, HttpSession session) { // 수정 안함
        String loginEmail = designerService.login(designerDTO);
        if (loginEmail != null) {
            session.setAttribute("loginEmail", loginEmail);
            return "main";
        }else{
            return "member/designer-login";
        }
    }
}
