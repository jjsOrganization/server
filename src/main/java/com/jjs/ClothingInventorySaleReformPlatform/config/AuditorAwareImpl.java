package com.jjs.ClothingInventorySaleReformPlatform.config;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {  // 상품의 createby를 위해 작성

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();  // 누가 로그인할고 있는지에 대한 정보
        String userId = "";
        if (authentication != null) {
            userId = authentication.getName();
        }
        return Optional.of(userId);
    }
}
