package com.jjs.ClothingInventorySaleReformPlatform.controller.chat;

import com.jjs.ClothingInventorySaleReformPlatform.jwt.filter.JwtAuthenticationFilter;
import com.jjs.ClothingInventorySaleReformPlatform.jwt.provider.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageDeliveryException;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

@Component
@RequiredArgsConstructor
@Slf4j
public class StompHandler implements ChannelInterceptor {

    private final RedisTemplate redisTemplate;
    private final JwtTokenProvider provider;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        // header 추출
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        log.info("accessor = {}", accessor);

        //CONNECT JWT 토큰 검사
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {

            String jwtToken = String.valueOf(accessor.getNativeHeader("Authorization")).substring(7);
            log.info("jwtToken = {}", jwtToken);

            // 토큰 검증 로직
            if(jwtToken == null || jwtToken.equals("null")){
                throw new MessageDeliveryException("메세지 예외");
            }

            if (!validateToken(jwtToken)) {
                throw new AccessDeniedException("Invalid token");
            }
        }
        return message;
    }

    private boolean validateToken(String token) {
        log.info("유효성 검사 시작");
        // 2. validateToken 으로 토큰 유효성 검사
        if (token != null && provider.validateToken(token)) {
            // Redis에 해당 accessToken logout 여부 확인
            String isLogout = (String) redisTemplate.opsForValue().get(token);
            log.info("redis 통과");

            if (ObjectUtils.isEmpty(isLogout)) {
                // 토큰이 유효할 경우 토큰에서 Authentication 객체를 가지고 와서 SecurityContext 에 저장
                Authentication authentication = provider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                return true;
            }
        }
        return false;
    }

}

