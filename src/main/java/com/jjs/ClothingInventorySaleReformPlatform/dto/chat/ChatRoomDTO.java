package com.jjs.ClothingInventorySaleReformPlatform.dto.chat;

import com.jjs.ClothingInventorySaleReformPlatform.domain.chat.Chat;
import com.jjs.ClothingInventorySaleReformPlatform.domain.chat.ChatMessage;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class ChatRoomDTO {

    private Long roomId;
    private String purchaserEmail;
    private String designerEmail;
    private String productCode;


    public static ChatRoomDTO convertToDTO(Chat chat) {

        ChatRoomDTO chatRoomDTO = new ChatRoomDTO();
        chatRoomDTO.setRoomId(chat.getId());
        chatRoomDTO.setPurchaserEmail(chat.getPurchaserEmail().getEmail());
        chatRoomDTO.setDesignerEmail(chat.getDesignerEmail().getEmail());

        return chatRoomDTO;
    }
}
