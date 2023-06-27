package com.example.sidemanagementbe.chat.controller;


import com.example.sidemanagementbe.chat.code.ChatCode;
import com.example.sidemanagementbe.chat.dto.ChatDto;
import com.example.sidemanagementbe.chat.dto.SystemMessageType;
import com.example.sidemanagementbe.chat.dto.WsResponse;
import com.example.sidemanagementbe.chat.exception.InvalidChatRoom;
import com.example.sidemanagementbe.chat.service.ChatService;
import com.example.sidemanagementbe.login.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

@Slf4j
@RequiredArgsConstructor
@Controller
public class WebSocketController {
    private final MemberService memberService;

    private final ChatService chatService;

    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat/{roomId}")
    public void sendMessage(@DestinationVariable Long roomId, ChatDto message) {
        Long memberId = message.getMemberId();
        String nickname = memberService.getMemberNickname(memberId);
        ChatCode chatCode = null;

        /**
         * SystemMessageType이 ONLINE이거나 OFFLINE일 경우 데이터베이스에 메시지가 저장되지 않게 함
         */
        if (message.getMessageType() != null) {
            if (message.getMessageType().equals(SystemMessageType.ONLINE)) {
                chatCode = ChatCode.SYSTEM_USER_ONLINE;
                WsResponse response = WsResponse.of(message, nickname, chatCode);
                messagingTemplate.convertAndSend("/sub/chat/" + roomId, response);
                return;
            } else if (message.getMessageType().equals(SystemMessageType.OFFLINE)) {
                chatCode = ChatCode.SYSTEM_USER_OFFLINE;
                WsResponse response = WsResponse.of(message, nickname, chatCode);
                messagingTemplate.convertAndSend("/sub/chat/" + roomId, response);
                return;
            }
        }

        chatCode = ChatCode.USER_CHAT_PUBLISH;

        // 해당 방과 해당 유저가 존재하는지 확인
        try {
            chatService.checkValidate(memberId, roomId);
        } catch (InvalidChatRoom e) {
            log.error("ERROR OCCURRED: " + e.getMessage());
            throw new InvalidChatRoom(e.getMessage());
        }

        chatService.saveMessage(message);

        WsResponse response = WsResponse.of(message, nickname, chatCode);

        messagingTemplate.convertAndSend("/sub/chat/" + roomId, response);
    }

    @SubscribeMapping("/joinRoom/{roomId}")
    public void joinRoom(@DestinationVariable String roomId, SimpMessageHeaderAccessor headerAccessor) {
        headerAccessor.getSessionAttributes().put("roomId", roomId);
    }

    // WebSocket 엔드포인트에서 발생하는 오류를 처리하기 위한 예외 핸들러
    @MessageExceptionHandler
    @SendTo(value = "/sub/error")
    public String handleWebSocketException(Exception exception) {
        exception.printStackTrace();
        // 예외를 처리하고 에러 메시지를 구독된 클라이언트에게 전송합니다
        return exception.getMessage();
    }
}