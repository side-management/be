package com.example.sidemanagementbe.chat.dto;


import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ChatDto {
    private Long teamId;
    private Long memberId;
    private String senderNickname;
    private String content;
    private SystemMessageType messageType;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
