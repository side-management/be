package com.example.sidemanagementbe.chat.entity;


import com.example.sidemanagementbe.chat.dto.SystemMessageType;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;


@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "saida_chat")
public class Chat {

    //teamId와 같음, 즉 roomId를 의미
    @Id
    @GeneratedValue
    Long id;

    @Column(nullable = false)
    private Long teamId;

    @Column(nullable = false)
    private Long memberId;

    private String senderNickname;

    private String content;

    private SystemMessageType messageType;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;


}
