package com.jjs.ClothingInventorySaleReformPlatform.repository.chat;

import com.jjs.ClothingInventorySaleReformPlatform.domain.chat.Chat;
import com.jjs.ClothingInventorySaleReformPlatform.dto.chat.ChatRoomDTO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRepository extends JpaRepository<Chat , Long> {
    List<ChatRoomDTO> findByPurchaserEmail(String purchaserEmail);
    List<ChatRoomDTO> findByDesignerEmail(String designerEmail);
}
