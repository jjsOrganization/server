package com.jjs.ClothingInventorySaleReformPlatform.service.kafka;

import com.jjs.ClothingInventorySaleReformPlatform.domain.chat.Chat;
import com.jjs.ClothingInventorySaleReformPlatform.domain.chat.ChatMessage;
import com.jjs.ClothingInventorySaleReformPlatform.dto.chat.ChatMessageDTO;
import com.jjs.ClothingInventorySaleReformPlatform.dto.chat.ChatRoomDTO;
import com.jjs.ClothingInventorySaleReformPlatform.repository.chat.ChatMessageRepository;
import com.jjs.ClothingInventorySaleReformPlatform.repository.chat.ChatRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ConsumerService {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMessageRepository chatMessageRepository;

    /**
     * 채팅을 받으면 DB에 저장하고 subscriber에게 전송합니다.
     * @param message
     */
    @Transactional
    @KafkaListener(topics = "${spring.kafka.template.default-topic}", groupId = "${spring.kafka.consumer.group-id}", concurrency = "3")
    public void listen(ChatMessageDTO message) {

        try{
            ChatMessage chatMessage = new ChatMessage();
            Chat chat = new Chat();
            chat.setId(message.getRoomId());

            chatMessage.setChat(chat);
            chatMessage.setMessage(message.getMessage());
            chatMessage.setSender(message.getSender()); // 로그인한 유저 명으로 변경해야함

            chatMessageRepository.save(chatMessage);

        }catch (Exception e){
            throw new RuntimeException("채팅 저장 에러");
        }

        log.info("전송 위치  = /sub/chat/room/{}", message.getRoomId());
        messagingTemplate.convertAndSend("/sub/chat/room/" + message.getRoomId() , message);
    }
}
