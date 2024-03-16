package com.jjs.ClothingInventorySaleReformPlatform.controller.chat;

import com.jjs.ClothingInventorySaleReformPlatform.controller.product.AuthenticationFacade;
import com.jjs.ClothingInventorySaleReformPlatform.dto.chat.ChatRoomDTO;
import com.jjs.ClothingInventorySaleReformPlatform.dto.response.Response;
import com.jjs.ClothingInventorySaleReformPlatform.service.chat.ChatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatRoomController {
    private final ChatService chatService;
    private final AuthenticationFacade authenticationFacade;
    private final Response response;

    // 채팅방 리스트 조회
    @GetMapping("/chatroom")
    public ResponseEntity<?> chatRoomList() {
        String currentUsername = authenticationFacade.getCurrentUsername();
        List<ChatRoomDTO> chatList = chatService.findAllRooms(currentUsername);

        return response.success(chatList);
    }

    @PostMapping("/chatroom")
    public ResponseEntity<?> createChatRoom(@RequestBody @Valid ChatRoomDTO chatRoomDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return response.fail("오류",HttpStatus.INTERNAL_SERVER_ERROR);
        }
        chatService.createChatRoomDTO(chatRoomDTO);

        return response.success();

    }

    // 채팅내역 조회 Mongo DB 연결 필요
//    @GetMapping("/chatroom/{roomNo}")
//    public ResponseEntity<?> chattingList(@PathVariable("roomNo") Long roomNo) {
//         chattingList = chatService.findRoomById(roomNo); //
//        return ResponseEntity.ok(chattingList);
//    }
}
