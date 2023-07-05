package com.example.sidemanagementbe.chat.code;

import lombok.Getter;

@Getter
public enum ChatCode {
    SYSTEM_USER_OUT("System", "C-1502", "유저 1명이 나갔습니다."),
    SYSTEM_USER_ENTER("System", "C-1501", "유저 1명이 참여했습니다."),
    SYSTEM_USER_KICKED_OUT("System", "C-1503", "유저 1명이 강퇴당했습니다."),
    ROOM_UPDATED("Room", "C-1505", "방 정보가 업데이트 되었습니다."),
    SYSTEM_USER_DELEGATED("System", "C-1504", "방장이 위임되었습니다."),
    USER_CHAT_PUBLISH("User", "C-1506", "유저 1명이 채팅을 보냈습니다."),
    SYSTEM_HOST_OUT("System", "C-1507", "방장이 방을 나갔습니다."),
    SYSTEM_USER_ONLINE("System", "C-1508", "유저 1명이 온라인이 되었습니다."),
    SYSTEM_USER_OFFLINE("System", "C-1509", "유저 1명이 오프라인이 되었습니다.");

    private final String type;
    private final String code;
    private final String message;

    ChatCode(String type, String code, String message) {
        this.type = type;
        this.code = code;
        this.message = message;
    }


}
