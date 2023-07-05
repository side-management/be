package com.example.sidemanagementbe.chat.entity;

import com.example.sidemanagementbe.chat.dto.SystemMessageType;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "saida_archive_chat")
public class ChatArchive {

    //team_id와 같음
    @Id
    @GeneratedValue
    Long id;

    @Column(nullable = false)
    private Long memberId;

    private String senderNickname;

    private String content;

    @Enumerated(EnumType.STRING)
    private SystemMessageType messageType;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
