package com.jjs.ClothingInventorySaleReformPlatform.dto.auth.request;

import lombok.Data;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@Data
public class UserLoginRequestDto {
    private String memberId;
    private String password;

    public UsernamePasswordAuthenticationToken toAuthentication() {
        return new UsernamePasswordAuthenticationToken(memberId, password);
    }
}
