package com.jjs.ClothingInventorySaleReformPlatform.controller.chat;

import com.jjs.ClothingInventorySaleReformPlatform.dto.chat.ChatMessageDTO;
import com.jjs.ClothingInventorySaleReformPlatform.service.kafka.ProducerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
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

