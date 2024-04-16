package com.jjs.ClothingInventorySaleReformPlatform.domain.chat.dto;

import com.jjs.ClothingInventorySaleReformPlatform.domain.chat.entity.ChatMessage;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class ChatMessageDTO {

    private Long roomId; // 단일 토픽, 단일 컨슈머 그룹 사용하기 때문에 채팅방 구분하려면 roomId 꼭 보내줘야함
    private String sender;
    private String message;
    private String time;

    public static ChatMessageDTO convertToDTO(ChatMessage chatMessage) {

        ChatMessageDTO chatMessageDTO = new ChatMessageDTO();
        chatMessageDTO.setRoomId(chatMessage.getChat().getId());
        chatMessageDTO.setSender(chatMessage.getSender());
        chatMessageDTO.setMessage(chatMessage.getMessage());
        chatMessageDTO.setTime(chatMessage.getSentAt());

        return chatMessageDTO;
    }

}
