package com.example.sidemanagementbe.chat.config;

import static com.example.sidemanagementbe.chat.entity.QChat.chat;

import com.example.sidemanagementbe.chat.entity.Chat;
import com.example.sidemanagementbe.chat.entity.ChatArchive;
import com.example.sidemanagementbe.chat.repository.ChatArchiveRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.List;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Transactional
@RequiredArgsConstructor
@Component
public class MessageArchiveSchedule {
    private final ChatArchiveRepository chatRepository;
    private final JPAQueryFactory queryFactory;


    @Scheduled(cron = "0 0 0 * * *") // 매일 밤 12시에 실행
    public long runAtMidnight() {
        List<Chat> fetch = queryFactory
                .selectFrom(chat)
                .where(chat.createdAt.loe(LocalDateTime.now().minusDays(7)))
                .fetch();

        for (Chat chat : fetch) {
            ChatArchive chatArchive = ChatArchive.builder()
                    .id(chat.getId())
                    .messageType(chat.getMessageType())
                    .memberId(chat.getMemberId())
                    .content(chat.getContent())
                    .createdAt(chat.getCreatedAt())
                    .updatedAt(chat.getUpdatedAt())
                    .build();

            chatRepository.save(chatArchive);
        }

        long count = queryFactory
                .delete(chat)
                .where(chat.createdAt.loe(LocalDateTime.now().minusDays(7)))
                .execute();
        
        return count;
    }
}
