package com.jjs.ClothingInventorySaleReformPlatform.service.chat;

import com.jjs.ClothingInventorySaleReformPlatform.domain.chat.Chat;
import com.jjs.ClothingInventorySaleReformPlatform.dto.chat.ChatRoomDTO;
import com.jjs.ClothingInventorySaleReformPlatform.repository.chat.ChatRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;



    public List<ChatRoomDTO> findAllRooms(String userEmail){
        //채팅방 생성 순서 최근 순으로 반환
        List<ChatRoomDTO> byPurchaserEmail = chatRepository.findByPurchaserEmail(userEmail);
        return byPurchaserEmail;
    }

//    public ChatRoomDTO findRoomById(Long id){
//        return chatRoomDTOMap.get(id);
//    }

    public void createChatRoomDTO(ChatRoomDTO roomDTO){
        Chat chat = new Chat();
        chat.setDesignerEmail(roomDTO.getDesignerEmail());
        chat.setPurchaserEmail(roomDTO.getPurchaserEmail());

        chatRepository.save(chat);
    }
}
