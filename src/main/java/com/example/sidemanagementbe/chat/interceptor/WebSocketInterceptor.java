package com.example.sidemanagementbe.chat.interceptor;

import com.example.sidemanagementbe.web.security.exception.JwtExpiredTokenException;
import com.example.sidemanagementbe.web.security.util.JwtTokenProvider;
import io.jsonwebtoken.MalformedJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageDeliveryException;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketInterceptor implements ChannelInterceptor {
    private final JwtTokenProvider jwtUtil;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        System.out.println("accessor:" + accessor.getCommand().toString());

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            /**
             * 처음 Connect를 하게 되면 요청이 두 개가 날라옴(Connect, Subscribe), 여기서 jwt 토큰은 Connect 요청에만 날라오게 됨
             * 그래서 Connect를 할 떄 jwt 검증을 하고 이후에는 검증하지 않음
             */
            log.info("--------------WebSocketInterceptor Sender------------");
            StompHeaderAccessor headerAccessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

            //jwt access 토큰 추출
            String authorizationHeader = String.valueOf(headerAccessor.getNativeHeader("Authorization"));
            String authorizationHeaderStr = authorizationHeader.replaceAll("\\[|\\]", "");
            StompCommand command = headerAccessor.getCommand();
            String token = authorizationHeaderStr.replace("Bearer ", "");

            if (authorizationHeader == null || !authorizationHeaderStr.startsWith("Bearer ")) {
                throw new MalformedJwtException("MalformedJwtException Occurred, 인증 header가 없습니다.");
            }
            if (!jwtUtil.isTokenValid(token)) {
                throw new JwtExpiredTokenException("Access token has expired");
            }

            if (command.equals(StompCommand.ERROR)) {
                throw new MessageDeliveryException("Message Error Occurred");
            }
        }
        return message;
    }


}