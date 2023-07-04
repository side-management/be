package com.example.sidemanagementbe.chat.code;

import lombok.Getter;

@Getter
public enum ErrorCode {
    JWT_PARSE_ERROR("W-101", "토큰 형식이 알맞지 않습니다.", "ERROR"),
    JWT_TOKEN_EXPIRED_ERROR("W-102", "토큰이 만료되었습니다.", "ERROR"),
    UNAUTHORIZED("W-103", "접근 권한이 없습니다.", "ERROR"),
    NATIVE_HEADER_NOT_FOUND("W-104", "인증 헤더를 찾을 수 없습니다.", "ERROR"),
    MESSAGE_DELIVERY_ERROR("W-105", "메시지 전송 오류", "ERROR"),
    ETC_ERROR("W-201", "기타 에러", "ERROR");

    private final String code;
    private final String message;

    private final String codeType;

    ErrorCode(String code, String message, String codeType) {
        this.code = code;
        this.message = message;
        this.codeType = codeType;
    }

}
