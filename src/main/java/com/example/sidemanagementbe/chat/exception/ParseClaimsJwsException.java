package com.example.sidemanagementbe.chat.exception;

public class ParseClaimsJwsException extends RuntimeException {

    public ParseClaimsJwsException(String message) {
        super(message);
    }

    public ParseClaimsJwsException(String message, Throwable cause) {
        super(message, cause);
    }
}
