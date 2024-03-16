package com.jjs.ClothingInventorySaleReformPlatform.dto.chat;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class ChatRoomDTO {
    private String roomId;
    private String purchaserEmail;
    private String designerEmail;


    public static ChatRoomDTO create(){
        ChatRoomDTO room = new ChatRoomDTO();

        room.roomId = UUID.randomUUID().toString();
        return room;
    }
}
