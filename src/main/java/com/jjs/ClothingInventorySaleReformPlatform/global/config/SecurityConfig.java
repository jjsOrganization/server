package com.jjs.ClothingInventorySaleReformPlatform.global.config;

import com.jjs.ClothingInventorySaleReformPlatform.global.jwt.filter.JwtAuthenticationFilter;
import com.jjs.ClothingInventorySaleReformPlatform.global.jwt.provider.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig{

    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate<String, Object> redisTemplate; // RedisTemplate 주입

    //AuthenticationManager Bean 등록
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {

        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // csrf disable
        http
                .csrf((auth) -> auth.disable());

        //From 로그인 방식 disable
        http
                .formLogin((auth) -> auth.disable());

        //http basic 인증 방식 disable
        http
                .httpBasic((auth) -> auth.disable());

        //경로별 인가 작업
        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/auth/login", "/auth/login-test", "/auth/reissue", "/", "/auth/join-purchaser", "/auth/join-seller", "/auth/join-designer",
                                "/designer/portfolio","/swagger-ui/**","/v3/api-docs/**", "/swagger-resources/**",
                                "/product/all/like/desc","/ws/chat/**", "/returnEstimateNumber/{reformRequest}", "/portfolio/reformOutput/detail/{progressNumber}",
                                "/portfolio/reformOutput/list", "/calculate/saveWater", "/portfolio/all", "/portfolio/{portfolioid}/detail",
                                "/product/all", "/product/all/{keyword}", "/product/all/{productId}", "/product/all/detail/{productId}", "/portfolio/{keyword}").permitAll()
                        .requestMatchers("/admin", "/auth/login-test", "/product/category/{categoryId}", "/product/all/detail/{productId}/**",
                                "/product/all/detail/{productId}/seller", "/user/role", "/auth/logout", "/auth/edit/**", "/auth/info/**",
                                "/auth/update/**", "/chatroom/{requestNo}/email", "/portfolio/reformOutput/upload/**").hasAnyRole("PURCHASER", "SELLER", "DESIGNER")
                        .requestMatchers("/product/seller/register", "/product/seller/register/{productId}", "/product/seller/register", "/seller/info",
                                "/order/seller-list/**", "/auth/update-seller/**").hasRole("SELLER")
                        .requestMatchers("/cart/purchaser/add/{productId}", "/cart/purchaser/**", "/reform-request/purchaser/**",
                                "/product/all/detail/{productId}/like", "/order/purchaser-list", "/auth/update-purchaser/address",
                                "/reform/purchaser/requests/**",
                                "/estimate/purchaser/**", "/estimate/purchaser/estimateForm/{requestNumber}").hasRole("PURCHASER")
                        .requestMatchers("/auth/update-designer/address","/portfolio/designer/**",
                                "/estimate/designer/estimateForm/**","/estimate/designer/requestForm/{requestNumber}",
                                "/progress/designer/setImg/**", "/portfolio/reformOutput/edit/**",
                                "/portfolio/reformOutput/designer/list").hasRole("DESIGNER")
                        .requestMatchers("/chat/**", "/chat", "/chatroom", "/progress/img/**").hasAnyRole("PURCHASER","DESIGNER")
                        .anyRequest().authenticated());

        http
                // 기존의 http 설정들...
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider, redisTemplate),
                        UsernamePasswordAuthenticationFilter.class);

        //세션 설정
        http
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {

        return new BCryptPasswordEncoder();
    }

}
