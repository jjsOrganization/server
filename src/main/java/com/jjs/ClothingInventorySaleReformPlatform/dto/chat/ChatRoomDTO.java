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
    private Long productCode;
    private Long requestId;


    public static ChatRoomDTO convertToDTO(Chat chat) {

        ChatRoomDTO chatRoomDTO = new ChatRoomDTO();
        chatRoomDTO.setRoomId(chat.getId());
        chatRoomDTO.setPurchaserEmail(chat.getPurchaserEmail().getEmail());
        chatRoomDTO.setDesignerEmail(chat.getDesignerEmail().getEmail());
        chatRoomDTO.setProductCode(chat.getProduct().getId());
        chatRoomDTO.setRoomId(chat.getReformRequest().getId());

        return chatRoomDTO;
    }
}
