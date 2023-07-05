package com.example.sidemanagementbe.chat.exception;

public class InvalidRefreshTokenException extends RuntimeException {
    public InvalidRefreshTokenException() {
        super("Invalid refresh token.");
    }
}
