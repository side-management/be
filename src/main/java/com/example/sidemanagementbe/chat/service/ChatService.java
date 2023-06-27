package com.example.sidemanagementbe.chat.service;

import com.example.sidemanagementbe.chat.dto.ChatDto;
import com.example.sidemanagementbe.chat.entity.Chat;
import com.example.sidemanagementbe.chat.exception.InvalidChatRoom;
import com.example.sidemanagementbe.chat.repository.ChatRepository;
import com.example.sidemanagementbe.login.repository.MemberRepository;
import com.example.sidemanagementbe.teammember.repository.TeamMemberRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class ChatService {

    private final ChatRepository chatRepository;
    private final MemberRepository memberRepository;

    private final TeamMemberRepository teamMemberRepository;


    public Long saveMessage(ChatDto chatDto) {
        Chat chatEntity = Chat.builder()
                .memberId(chatDto.getMemberId())
                .content(chatDto.getContent())
                .messageType(chatDto.getMessageType())
                .senderNickname(chatDto.getSenderNickname())
                .build();

        chatRepository.save(chatEntity);

        return chatDto.getTeamId();
    }

    /**
     * 팀별로 대화방 생성 roomId = teamId
     */
    public void checkValidate(Long memberId, Long roomId) {
        Long teamId = teamMemberRepository.findTeamIdByMemberId(roomId, memberId);
        if (!teamId.equals(roomId)) {
            throw new InvalidChatRoom("사용자가 해당 팀에 속하지 않습니다.");
        }
    }
}
