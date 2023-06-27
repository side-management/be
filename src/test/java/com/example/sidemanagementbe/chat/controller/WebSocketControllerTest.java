package com.example.sidemanagementbe.chat.controller;


import java.lang.reflect.Type;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

@SpringBootTest
class WebSocketControllerTest {

    @Test
    public void testStompClient() {
        WebSocketStompClient stompClient = new WebSocketStompClient(new StandardWebSocketClient());
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        StompSessionHandler sessionHandler = new StompSessionHandlerAdapter() {
            @Override
            public void handleException(StompSession session, StompCommand command, StompHeaders headers,
                                        byte[] payload, Throwable exception) {
                super.handleException(session, command, headers, payload, exception);
                System.out.println("--------handle Exception 발생------");
            }

            @Override
            public void handleTransportError(StompSession session, Throwable exception) {
                super.handleTransportError(session, exception);
                System.out.println("--------handleTransportError 발생------");
            }

            @Override
            public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
                System.out.println("연결 성공!");
                // 연결 성공 후에 실행되는 로직
                session.send("/pub/chat/enter", "Hello, server!");  // 메시지 보내기
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                session.send("/pub/chat/enter", "Hello, server!");  // 메시지 보내기
                System.out.println("test 테스트 1");
                session.send("/pub/chat/enter", "Hello, server!");  // 메시지 보내기
                System.out.println("test 테스트 2");
                session.send("/pub/chat/enter", "Hello, server!");  // 메시지 보내기
                System.out.println("test 테스트 3");
                session.send("/pub/chat/enter", "Hello, server!");  // 메시지 보내기
                System.out.println("test 테스트 4");
                session.send("/pub/chat/enter", "Hello, server!");  // 메시지 보내기
                System.out.println("test 테스트 5");
                session.send("/pub/chat/enter", "Hello, server!");  // 메시지 보내기
                System.out.println("test 테스트 6");

            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                // 서버에서 응답이 도착했을 때 실행되는 로직
                System.out.println("Received: " + payload);
            }

            @Override
            public Type getPayloadType(StompHeaders headers) {
                return String.class;
            }
        };

        String serverUrl = "ws://localhost:8080/ws/websocket";  // STOMP WebSocket 서버의 URL
        stompClient.connect(serverUrl, sessionHandler);

    }

}