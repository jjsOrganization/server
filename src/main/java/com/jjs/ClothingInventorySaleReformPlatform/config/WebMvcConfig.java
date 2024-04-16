package com.jjs.ClothingInventorySaleReformPlatform.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${server.address}")
    String ec2Host;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://"+ ec2Host +":3000" , "http://"+ec2Host+"/","http://localhost:3000")
                .allowedMethods("OPTIONS","GET","POST","PUT","DELETE","PATCH");
    }
}

