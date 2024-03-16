package com.jjs.ClothingInventorySaleReformPlatform.dto.chat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class ChatMessageDTO {
    public enum MessageType {
        ENTER, TALK
    }

    private String roomId;
    private String writer;
    private String message;
}
