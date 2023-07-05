package com.example.sidemanagementbe.chat.config;

import com.example.sidemanagementbe.chat.entity.Chat;
import com.example.sidemanagementbe.chat.repository.ChatArchiveRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static com.example.sidemanagementbe.chat.entity.QChat.chat;

@Transactional
@RequiredArgsConstructor
@Component
public class MessageArchiveSchedule {
    private final ChatArchiveRepository chatRepository;
    private final JPAQueryFactory queryFactory;

    @Scheduled(cron = "0 0 0 * * *") // 매일 밤 12시에 실행
    // @Scheduled(cron = "0 * * * * *")
    public long runAtMidnight() {
        List<Chat> fetch = queryFactory
                .selectFrom(chat)
                .where(chat.createdAt.loe(LocalDateTime.now().minusDays(7)))
                .fetch();

        long count = queryFactory
                .delete(chat)
                .where(chat.createdAt.loe(LocalDateTime.now().minusDays(7)))
                .execute();

        chatRepository.saveAll(fetch);

        return count;
    }
}
