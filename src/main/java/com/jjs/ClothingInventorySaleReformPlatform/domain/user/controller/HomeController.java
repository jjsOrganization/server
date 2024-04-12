package com.jjs.ClothingInventorySaleReformPlatform.domain.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseBody
public class HomeController {

    @GetMapping("/")
    public String mainP() {
        return "main Controller";
    }
}
