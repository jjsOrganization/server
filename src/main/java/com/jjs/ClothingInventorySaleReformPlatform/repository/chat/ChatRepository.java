package com.jjs.ClothingInventorySaleReformPlatform.repository.chat;

import com.jjs.ClothingInventorySaleReformPlatform.domain.chat.Chat;
import com.jjs.ClothingInventorySaleReformPlatform.domain.user.DesignerInfo;
import com.jjs.ClothingInventorySaleReformPlatform.domain.user.PurchaserInfo;
import com.jjs.ClothingInventorySaleReformPlatform.dto.chat.ChatRoomDTO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatRepository extends JpaRepository<Chat , Long> {
    List<Chat> findByPurchaserEmail(PurchaserInfo purchaserEmail);
//    List<ChatRoomDTO> findByDesignerEmail(DesignerInfo designerEmail);

//    Optional<Chat> findByIdAndDesignerEmailAndPurchaserEmail(Long id, PurchaserInfo purchaserEmail);
}
