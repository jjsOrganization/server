package com.jjs.ClothingInventorySaleReformPlatform.controller.chat;

import com.jjs.ClothingInventorySaleReformPlatform.controller.product.AuthenticationFacade;
import com.jjs.ClothingInventorySaleReformPlatform.dto.chat.ChatMessageDTO;
import com.jjs.ClothingInventorySaleReformPlatform.dto.chat.ChatRoomDTO;
import com.jjs.ClothingInventorySaleReformPlatform.dto.chat.response.ChatEmailDTO;
import com.jjs.ClothingInventorySaleReformPlatform.dto.response.Response;
import com.jjs.ClothingInventorySaleReformPlatform.repository.chat.ChatMessageRepository;
import com.jjs.ClothingInventorySaleReformPlatform.service.chat.ChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "채팅 API", description = "채팅방 생성, 조회, 채팅 내역 조회 API 입니다.")
public class ChatRoomController {
    private final ChatService chatService;
    private final AuthenticationFacade authenticationFacade;
    private final Response response;
    private final ChatMessageRepository chatMessageRepository;


    // 채팅방 리스트 조회

    @GetMapping("/chatroom")
    @Operation(description = "로그인된 사용자가 참여중인 채팅방의 리스트를 조회합니다.", summary = "채팅방 리스트 조회 API")
    public ResponseEntity<?> chatRoomList() {
        String currentUsername = authenticationFacade.getCurrentUsername();
        List<ChatRoomDTO> chatList = chatService.findAllRooms(currentUsername);
        return response.success(chatList);
    }

    @PostMapping("/chatroom") // "채팅방 생성" 버튼에 이벤트로 설정
    @Operation(summary = "채팅방 생성 요청 API", description = "채팅방 생성을 요청하는 메소드입니다." +
            "파라미터 => designerEmail(디자이너 이메일), purchaserEmail(구매자 이메일), productCode(상품 id)")
    public ResponseEntity<?> createChatRoom(@RequestBody @Valid ChatRoomDTO chatRoomDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return response.fail("오류",HttpStatus.INTERNAL_SERVER_ERROR);
        }
        ChatRoomDTO createRoomId = chatService.createChatRoomDTO(chatRoomDTO);

        return response.success(createRoomId);
    }

    @PostMapping("/chatroom/{roomNo}")
    @Operation(summary = "채팅방 접속 종료 API", description = " 채팅방 접속 종료 메소드입니다(수정중)")
    public ResponseEntity<?> disconnectChat(@PathVariable Long roomNo, @RequestParam("email") String email) throws IllegalAccessException {
        chatService.disconnectChatRoom(roomNo, email);
        return response.success("채팅방 삭제 성공");
    }

    // 채팅내역 조회 Mongo DB 연결 필요 (현재 MySQL로 구현)
    @GetMapping("/chatroom/{roomNo}")
    @Operation(summary = "채팅방 내역 조회", description = " 방 번호에 해당하는 채팅 내역을 불러옵니다.")
    public ResponseEntity<?> chattingList(@PathVariable("roomNo") Long roomNo) {
        List<ChatMessageDTO> chatList = chatService.getChatList(roomNo);
        return response.success(chatList);
    }

    @GetMapping("/chatroom/{requestNo}/email")
    @Operation(summary = "이메일 조회", description = "요청서로 디자이너 이메일과 구매자 이메일 조회 메서드")
    public ResponseEntity<?> chattingEmail(@PathVariable("requestNo") Long requestNo) {
        ChatEmailDTO chatEmail = chatService.getEmailByRequests(requestNo);
        //ReformRequest emails = chatService.getEmailByRequests(requestNo);
        return response.success(chatEmail, "디자이너, 구매자 이메일 조회 완료", HttpStatus.OK);
    }

}
