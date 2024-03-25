package com.jjs.ClothingInventorySaleReformPlatform.service.kafka;

import com.jjs.ClothingInventorySaleReformPlatform.dto.chat.ChatMessageDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProducerService {
    private final KafkaTemplate<String, ChatMessageDTO> kafkaTemplate;

    public void sendMessage(String topic, ChatMessageDTO message) {
        kafkaTemplate.send(topic,message);
    }
    
}
