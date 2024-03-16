package com.jjs.ClothingInventorySaleReformPlatform.service.kafka;

import com.jjs.ClothingInventorySaleReformPlatform.dto.chat.ChatMessageDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ConsumerService {

    private final SimpMessagingTemplate messagingTemplate;
    @KafkaListener(topics = "${spring.kafka.template.default-topic}", groupId = "${spring.kafka.consumer.group-id}", concurrency = "3")
    public void listen(String message) {
        log.info("Received message : {}", message);
        messagingTemplate.convertAndSend("/sub/chat/room/1" , message);
    }
}
