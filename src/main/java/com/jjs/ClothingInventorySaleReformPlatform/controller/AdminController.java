package com.jjs.ClothingInventorySaleReformPlatform.controller;

import com.jjs.ClothingInventorySaleReformPlatform.jwt.JWTUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
@ResponseBody
public class AdminController {

    private final JWTUtil jwtUtil;
    private final Logger logger = LoggerFactory.getLogger(AdminController.class);

    public AdminController(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/admin")
    public ResponseEntity<?> getAdminPage(HttpServletRequest request) {
        // JWT 토큰 추출
        String token = extractToken(request);


        // 토큰에서 정보 추출
        String username = jwtUtil.getUsername(token);


        // 디버깅 용도
        logger.info("Accessed Admin Page: Username: {}", username);

        // 정보 출력
        Map<String, String> userInfo = new HashMap<>();
        userInfo.put("Username", username);

        // 추가 정보를 맵에 저장

        return ResponseEntity.ok(userInfo);
    }

    private String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    /*
    @GetMapping("/admin")
    public String adminP() {
        return "admin Controller";
    }

     */
}
