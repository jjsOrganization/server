package com.jjs.ClothingInventorySaleReformPlatform.service.chat;

import com.jjs.ClothingInventorySaleReformPlatform.controller.product.AuthenticationFacade;
import com.jjs.ClothingInventorySaleReformPlatform.domain.chat.Chat;
import com.jjs.ClothingInventorySaleReformPlatform.domain.chat.ChatMessage;
import com.jjs.ClothingInventorySaleReformPlatform.domain.product.Product;
import com.jjs.ClothingInventorySaleReformPlatform.domain.reform.reformrequest.ReformRequest;
import com.jjs.ClothingInventorySaleReformPlatform.domain.user.DesignerInfo;
import com.jjs.ClothingInventorySaleReformPlatform.domain.user.PurchaserInfo;
import com.jjs.ClothingInventorySaleReformPlatform.dto.chat.ChatMessageDTO;
import com.jjs.ClothingInventorySaleReformPlatform.dto.chat.ChatRoomDTO;
import com.jjs.ClothingInventorySaleReformPlatform.dto.chat.response.ChatEmailDTO;
import com.jjs.ClothingInventorySaleReformPlatform.repository.chat.ChatMessageRepository;
import com.jjs.ClothingInventorySaleReformPlatform.repository.chat.ChatRepository;
import com.jjs.ClothingInventorySaleReformPlatform.repository.reform.reformrequest.ReformRequestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor

public class ChatService {
    private final AuthenticationFacade authenticationFacade;
    private final ChatRepository chatRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ReformRequestRepository reformRequestRepository;

    public List<ChatRoomDTO> findAllRooms(String userEmail){
        String currentUserType = authenticationFacade.getCurrentUserType();
        log.info("currentUserType = {}", currentUserType);

        if(currentUserType.equals("[ROLE_PURCHASER]")){
            PurchaserInfo purchaserInfo = new PurchaserInfo();
            purchaserInfo.setEmail(userEmail);

            //채팅방 생성 순서 최근 순으로 반환
            return getChatRoomDTO(chatRepository.findByPurchaserEmail(purchaserInfo));

        } else if (currentUserType.equals("[ROLE_DESIGNER]")) {
            DesignerInfo designerInfo = new DesignerInfo();
            designerInfo.setEmail(userEmail);

            //채팅방 생성 순서 최근 순으로 반환
            return getChatRoomDTO(chatRepository.findByDesignerEmail(designerInfo));
        } else{
            throw new IllegalArgumentException("판매자는 채팅 목록을 조회할 수 없습니다.");
        }
    }

    private List<ChatRoomDTO> getChatRoomDTO(List<Chat> chatRepository) {
        List<ChatRoomDTO> byUserEmail = chatRepository
                .stream()
                .map(ChatRoomDTO::convertToDTO)
                .collect(Collectors.toList());
        return byUserEmail;
    }

    @Transactional
    public ChatRoomDTO createChatRoomDTO(ChatRoomDTO roomDTO){
        Chat chat = new Chat();
        DesignerInfo designerInfo = new DesignerInfo();
        designerInfo.setEmail(roomDTO.getDesignerEmail());

        PurchaserInfo purchaserInfo = new PurchaserInfo();
        purchaserInfo.setEmail(roomDTO.getPurchaserEmail());

        Product product = new Product();
        product.setId(roomDTO.getProductCode());

        chat.setDesignerEmail(designerInfo);
        chat.setPurchaserEmail(purchaserInfo);
        chat.setProduct(product);

        Chat createdChatRoom = chatRepository.save(chat);

        // 저장된 채팅방 객체 반환
        ChatRoomDTO chatRoomDTO = new ChatRoomDTO();

        chatRoomDTO.setRoomId(createdChatRoom.getId());
        chatRoomDTO.setPurchaserEmail((createdChatRoom.getPurchaserEmail().getEmail()));
        chatRoomDTO.setDesignerEmail((createdChatRoom.getDesignerEmail().getEmail()));
        chatRoomDTO.setProductCode((createdChatRoom.getProduct().getId()));

        return chatRoomDTO;
    }

    @Transactional
    public List<ChatMessageDTO> getChatList(Long RoomId){
        List<ChatMessage> byChat = chatMessageRepository.findByChat_Id(RoomId);

        List<ChatMessageDTO> chatList = byChat.stream()
                .map(ChatMessageDTO::convertToDTO)
                .collect(Collectors.toList());

        return chatList;
    }

    /**
     *
     * @param chatroomNo
     * @param email
     * @throws IllegalAccessException
     * 디자이너 이메일, 구매자 이메일 모두 파라미터로 받아서 확인할 지 논의 필요
     */

    public void disconnectChatRoom(Long chatroomNo, String email) throws IllegalAccessException {
        Chat chatRoom = chatRepository.findById(chatroomNo).orElseThrow(IllegalAccessException::new);
        chatRepository.delete(chatRoom);
    }

    public ChatEmailDTO getEmailByRequests(Long requestNumber) {
        ReformRequest reformRequest = reformRequestRepository.findAllById(requestNumber);
        ChatEmailDTO chatEmailDTO = new ChatEmailDTO();
        chatEmailDTO.setPurchaserEmail(reformRequest.getPurchaserEmail().getEmail());
        chatEmailDTO.setDesignerEmail(reformRequest.getDesignerEmail().getEmail());
        return chatEmailDTO;
    }
}
