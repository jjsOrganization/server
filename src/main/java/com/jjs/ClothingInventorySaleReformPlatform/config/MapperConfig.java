package com.jjs.ClothingInventorySaleReformPlatform.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {

    @Bean
    public ModelMapper modelMapper() {  // bean으로 객체를 만들어서 사용하면 private final ModelMapper modelMapper;로 사용 가능
        return new ModelMapper();
    }
}
