package com.example.sidemanagementbe.chat.handler;

import com.example.sidemanagementbe.chat.code.ErrorCode;
import com.example.sidemanagementbe.chat.code.ResponseErrorCode;
import com.example.sidemanagementbe.chat.dto.Status;
import com.example.sidemanagementbe.web.security.exception.JwtExpiredTokenException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dockerjava.api.exception.UnauthorizedException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageDeliveryException;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.StompSubProtocolErrorHandler;

@Slf4j
@Component
public class StompExceptionHandler extends StompSubProtocolErrorHandler {

    private static final byte[] EMPTY_PAYLOAD = new byte[0];

    public StompExceptionHandler() {
        super();
    }

    @Override
    public Message<byte[]> handleClientMessageProcessingError(Message<byte[]> clientMessage,
                                                              Throwable ex) {
        log.info("------------------StompExceptionHandler---------------");
        final Throwable exception = converterTrowException(ex);

        if (exception instanceof UnauthorizedException) {
            String errorMessage = "An error occurred while doing something.";
            log.error("[UnauthorizedException] - {}: {}", errorMessage, exception.getMessage(), exception);
            return handleException(clientMessage, exception, ErrorCode.UNAUTHORIZED);
        }

        if (exception instanceof SignatureException) {
            String errorMessage = "An error occurred while doing something.";
            log.error("[SignatureException] - {}: {}", errorMessage, exception.getMessage(), exception);
            return handleException(clientMessage, exception, ErrorCode.JWT_PARSE_ERROR);
        }

        if (exception instanceof JwtExpiredTokenException) {
            String errorMessage = "An error occurred while doing something.";
            log.error("[JwtExpiredTokenException] - {}: {}", errorMessage, exception.getMessage(), exception);
            return handleException(clientMessage, exception, ErrorCode.JWT_TOKEN_EXPIRED_ERROR);
        }

        if (exception instanceof MalformedJwtException) {
            String errorMessage = "An error occurred while doing something.";
            log.error("[MalformedJwtException] - {}: {}", errorMessage, exception.getMessage(), exception);
            return handleException(clientMessage, exception, ErrorCode.NATIVE_HEADER_NOT_FOUND);
        }

        if (exception instanceof MessageDeliveryException) {
            String errorMessage = "An error occurred while doing something.";
            log.error("[MessageDeliveryException] - {}: {}", errorMessage, exception.getMessage(), exception);
            return handleException(clientMessage, exception, ErrorCode.MESSAGE_DELIVERY_ERROR);
        }
        System.out.println("exception:" + exception.getMessage());

        return super.handleClientMessageProcessingError(clientMessage, ex);

    }

    private Throwable converterTrowException(final Throwable exception) {
        if (exception instanceof MessageDeliveryException) {
            return exception.getCause();
        }
        return exception;
    }

    private Message<byte[]> handleException(Message<byte[]> clientMessage,
                                            Throwable ex, ErrorCode errorCode) {

        return prepareErrorMessage(clientMessage, ex.getMessage(), errorCode);

    }

    private Message<byte[]> prepareErrorMessage(final Message<byte[]> clientMessage,
                                                final String message, final ErrorCode errorCode) {

        final StompHeaderAccessor accessor = StompHeaderAccessor.create(StompCommand.ERROR);

        accessor.setLeaveMutable(true);
        setReceiptIdForClient(clientMessage, accessor);

        MessageHeaders headers = new MessageHeaders(null);

        return MessageBuilder.createMessage(
                message != null ? errorCodeToJsonResponse(errorCode).getBytes(StandardCharsets.UTF_8)
                        : EMPTY_PAYLOAD,
                accessor.getMessageHeaders()
        );
    }

    private String errorCodeToJsonResponse(ErrorCode errorCode) {
        // ObjectMapper를 사용하여 Java 객체를 JSON 문자열로 변환
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = null;

        Status status = Status.builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .type(errorCode.getCodeType())
                .build();

        ResponseErrorCode response = new ResponseErrorCode(status);

        try {
            jsonString = objectMapper.writeValueAsString(response);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return jsonString;
    }

    private void setReceiptIdForClient(final Message<byte[]> clientMessage,
                                       final StompHeaderAccessor accessor) {

        if (Objects.isNull(clientMessage)) {
            return;
        }

        final StompHeaderAccessor clientHeaderAccessor = MessageHeaderAccessor.getAccessor(
                clientMessage, StompHeaderAccessor.class);

        final String receiptId =
                Objects.isNull(clientHeaderAccessor) ? null : clientHeaderAccessor.getReceipt();

        if (receiptId != null) {
            accessor.setReceiptId(receiptId);
        }
    }

    //2
    @Override
    protected Message<byte[]> handleInternal(StompHeaderAccessor errorHeaderAccessor,
                                             byte[] errorPayload, Throwable cause,
                                             StompHeaderAccessor clientHeaderAccessor) {

        return MessageBuilder.createMessage(errorPayload, errorHeaderAccessor.getMessageHeaders());

//        return super.handleInternal(errorHeaderAccessor, errorPayload, cause, clientHeaderAccessor);
    }
}