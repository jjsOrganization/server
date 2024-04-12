package com.jjs.ClothingInventorySaleReformPlatform.domain.chat.repository;

import com.jjs.ClothingInventorySaleReformPlatform.domain.chat.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    List<ChatMessage> findByChat_Id(Long chatRoomId);
}
