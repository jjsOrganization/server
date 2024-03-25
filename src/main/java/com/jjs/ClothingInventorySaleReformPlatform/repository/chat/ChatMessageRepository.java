package com.jjs.ClothingInventorySaleReformPlatform.repository.chat;

import com.jjs.ClothingInventorySaleReformPlatform.domain.chat.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    List<ChatMessage> findByChat_Id(Long chatRoomId);
}
