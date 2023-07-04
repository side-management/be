package com.example.sidemanagementbe.chat.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.SettableListenableFuture;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.lang.reflect.Type;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class WebSocketControllerTest {


    @Test
    public void testWebSocketConnection() throws Exception {
        WebSocketStompClient stompClient = new WebSocketStompClient(new StandardWebSocketClient());
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        SettableListenableFuture<StompSession> sessionFuture = new SettableListenableFuture<>();
        StompSessionHandlerAdapter sessionHandler = new TestSessionHandler(sessionFuture);

        String url = "ws://localhost:8080/ws/websocket";  // WebSocket 엔드포인트 URL을 여기에 입력하세요.
        // JWT 토큰을 생성하여 헤더에 추가합니다.
        String jwtToken = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiVVNFUiIsImlkIjoiMTE0IiwicmFuZG9tIFVVSUQiOiIyNjBlMGIxMi1iMzIzLTRlYWYtOTNhMS1jYzA1YzM0NDI2MmMiLCJleHAiOjE2ODgzNzExNzd9.iCxc-n15FUDApAK2XEsb8n3G8ZincE6tuy4nc2yLaB4";

        WebSocketHttpHeaders headers = new WebSocketHttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken);

        ListenableFuture<StompSession> connectFuture = stompClient.connect(url, headers, sessionHandler);
        StompSession stompSession = sessionFuture.get(10, TimeUnit.SECONDS);

        assertThat(stompSession).isNotNull();
        assertThat(connectFuture.isDone()).isTrue();
    }

    private class TestSessionHandler extends StompSessionHandlerAdapter {

        private final SettableListenableFuture<StompSession> sessionFuture;

        public TestSessionHandler(SettableListenableFuture<StompSession> sessionFuture) {
            this.sessionFuture = sessionFuture;
        }

        @Override
        public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
            sessionFuture.set(session);
        }
    }
}