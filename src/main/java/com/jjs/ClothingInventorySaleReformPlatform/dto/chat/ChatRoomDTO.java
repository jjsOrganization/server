package com.jjs.ClothingInventorySaleReformPlatform.dto.chat;

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

}
