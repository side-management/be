package com.example.sidemanagementbe.chat.entity;


import com.example.sidemanagementbe.chat.dto.SystemMessageType;
import com.example.sidemanagementbe.common.entity.BaseEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
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
@PrimaryKeyJoinColumn(name = "team_id")
@Table(name = "saida_chat")
public class Chat extends BaseEntity {

    @Column(nullable = false)
    private Long memberId;

    private String senderNickname;

    private String content;

    private SystemMessageType messageType;


}
