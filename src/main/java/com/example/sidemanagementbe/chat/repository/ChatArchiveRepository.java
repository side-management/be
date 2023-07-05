package com.example.sidemanagementbe.chat.repository;

import com.example.sidemanagementbe.chat.entity.ChatArchive;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatArchiveRepository extends JpaRepository<ChatArchive, Long> {
}
