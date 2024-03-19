package com.jjs.ClothingInventorySaleReformPlatform.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@RequiredArgsConstructor
@EnableRedisRepositories
public class RedisRepositoryConfig {

    /**
     * 맥에서 Redis homebrew로 설치한 경우 서버 실행 방법
     * 1. 서버 실행 : brew services start redis
     * 2. cli 접근 : redis-cli
     * 2-1. 현재 key 전체 조회 : keys *
     * 2-2. key에 대한 value 조회(예시) : get RT:Test1@test.com -> value(RT)값 조회
     * Redis 서버 종료
     * 1. 서버 종료 : brew services stop redis
     */

    private final RedisProperties redisProperties;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(redisProperties.getHost(), redisProperties.getPort());  // properties에 저장한 host, port를 가지고 와서 연결
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        return redisTemplate;
    }
}
