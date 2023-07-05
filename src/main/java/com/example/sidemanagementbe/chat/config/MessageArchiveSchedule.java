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

@RequiredArgsConstructor
@Component
public class MessageArchiveSchedule {
    private final ChatArchiveRepository chatArchiveRepository;
    private final JPAQueryFactory queryFactory;

    @Scheduled(cron = "0 0 0 * * *") // 매일 밤 12시에 실행
    @Transactional
    public long runAtMidnight() {
        //일주일 전보다 과거인 데이터를 지우기 위한 임계 시간
        LocalDateTime pastWeekThreshold = LocalDateTime.now().minusDays(7);
        List<Chat> fetch = queryFactory
                .selectFrom(chat)
                .where(chat.createdAt.loe(pastWeekThreshold))
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

            chatArchiveRepository.save(chatArchive);
        }

        long count = queryFactory
                .delete(chat)
                .where(chat.createdAt.loe(pastWeekThreshold))
                .execute();

        return count;
    }
}
