package com.example.sidemanagementbe.chat.dto;

import com.example.sidemanagementbe.chat.code.ChatCode;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class WsResponse {
    private ChatDto chatDto;
    private Status status;

    public static WsResponse of(ChatDto message, String senderNickname, ChatCode chatCode) {

        Status status = Status.builder()
                .message(chatCode.getMessage())
                .code(chatCode.getCode())
                .type(chatCode.getType())
                .build();
        WsResponse response = WsResponse.builder()
                .chatDto(message)
                .status(status)
                .build();
        return response;
    }


}
