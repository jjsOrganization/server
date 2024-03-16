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

    private final SimpMessagingTemplate template; //특정 Broker로 메세지를 전달
    private final ProducerService producerService;
    private final NewTopic topic;

    //Client가 SEND할 수 있는 경로
    //stompConfig에서 설정한 applicationDestinationPrefixes와 @MessageMapping 경로가 병합됨
    //"/pub/chat/enter"
    @MessageMapping(value = "/chat/enter")
    public void enter(ChatMessageDTO message) {
        message.setMessage(message.getWriter() + "님이 채팅방에 참여하였습니다.");
        template.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);
    }

    @MessageMapping("/chat/message")
    public void receiveMessage(@Payload String message) { // @Payload는 메시지의 본문을 메소드의 매개변수로 직접 매핑하는데 사용됨.
        producerService.sendMessage(topic.name(),message);
    }
}

