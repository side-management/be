package com.example.sidemanagementbe.chat.config;

import static com.example.sidemanagementbe.chat.entity.QChat.chat;

import com.example.sidemanagementbe.chat.entity.Chat;
import com.example.sidemanagementbe.chat.repository.ChatRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class MessageArchiveSchedule {
    private final ChatRepository chatRepository;
    private final JPAQueryFactory queryFactory;

    @Scheduled(cron = "0 0 0 * * *") // 매일 밤 12시에 실행
    public void runAtMidnight() {
        List<Chat> fetch = queryFactory
                .selectFrom(chat)
                .where(chat.createdAt.between(LocalDateTime.now().minusDays(7), LocalDateTime.now()))
                .fetch();

        long count = queryFactory
                .delete(chat)
                .where(chat.createdAt.between(LocalDateTime.now().minusDays(7), LocalDateTime.now()))
                .execute();
    }
}
