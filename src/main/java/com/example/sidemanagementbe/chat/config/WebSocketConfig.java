package com.example.sidemanagementbe.chat.config;


import com.example.sidemanagementbe.chat.handler.StompExceptionHandler;
import com.example.sidemanagementbe.chat.interceptor.WebSocketInterceptor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Slf4j
@RequiredArgsConstructor
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    private final WebSocketInterceptor stompHandler;
    private final StompExceptionHandler exceptionHandler;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        log.info("------------registerStompEndpoints-------");
        //stomp의 접속 주소
        registry.setErrorHandler(exceptionHandler).addEndpoint("/ws").setAllowedOriginPatterns("*").withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        log.info("------------configureMessageBroker-------");
        //클라이언트의 send요청 처리
        registry.setApplicationDestinationPrefixes("/pub");
        //sub하는 클라이언트에게 메시지 전달
        registry.enableSimpleBroker("/sub");

    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        log.info("---------------configureClientInboundChannel---------------");
        registration.interceptors(stompHandler);
    }

//    @Override
//    public void configureClientInboundChannel(ChannelRegistration registration) {
//        log.info("------------configureClientInboundChannel-------");
//        registration.interceptors(webSocketInterceptor);
//    }
    //    @Override
//    public void registerStompEndpoints(StompEndpointRegistry registry) {
//        registry.addEndpoint("/chat") // 기본 엔드포인트 설정
//                .setAllowedOrigins("*") // CORS 허용을 위한 설정
//                .withSockJS(); // SockJS 지원을 위한 설정 (옵션)
//
//        // 동적 엔드포인트 추가
//        registry.addEndpoint("/chat/{teamId}") // {roomId}은 동적인 대화방 식별자입니다.
//                .setAllowedOrigins("*") // CORS 허용을 위한 설정
//                .withSockJS(); // SockJS 지원을 위한 설정 (옵션)
//    }
}
