package com.example.sidemanagementbe.chat.entity;


import com.example.sidemanagementbe.chat.dto.SystemMessageType;
import lombok.*;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.persistence.*;
import java.time.LocalDateTime;
@Qualifier("secondary")
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "archive_saida_chat")
public class ChatArchive {

    //teamId와 같음, 즉 roomId를 의미
    @Id
    @GeneratedValue
    Long id;

    @Column(nullable = false)
    private Long memberId;

    private String senderNickname;

    private String content;

    private SystemMessageType messageType;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
