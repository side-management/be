package com.example.sidemanagementbe.chat.exception;

public class InvalidChatRoom extends RuntimeException {

    public InvalidChatRoom(String message) {
        super(message);
    }

    public InvalidChatRoom(String message, Throwable cause) {
        super(message, cause);
    }
}
