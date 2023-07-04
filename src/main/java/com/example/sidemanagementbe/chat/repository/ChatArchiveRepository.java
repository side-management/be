package com.example.sidemanagementbe.chat.repository;

import com.example.sidemanagementbe.chat.entity.Chat;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Qualifier("secondary")
@Repository
public interface ChatArchiveRepository extends JpaRepository<Chat, Long> {
}
