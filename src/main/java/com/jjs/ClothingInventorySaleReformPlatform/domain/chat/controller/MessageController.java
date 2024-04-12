package com.jjs.ClothingInventorySaleReformPlatform.domain.chat.controller;

import com.jjs.ClothingInventorySaleReformPlatform.domain.chat.dto.ChatMessageDTO;
import com.jjs.ClothingInventorySaleReformPlatform.global.kafka.ProducerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class MessageController {

    private final ProducerService producerService;
    private final NewTopic topic;

    @MessageMapping("/chat/message")
    public void receiveMessage(@Payload ChatMessageDTO message) { // @Payload는 메시지의 본문을 메소드의 매개변수로 직접 매핑하는데 사용됨.
        log.info(topic.name());
        producerService.sendMessage(topic.name(),message);
    }
}

