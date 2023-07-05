package com.example.sidemanagementbe.chat.controller.config;

import com.example.sidemanagementbe.chat.config.MessageArchiveSchedule;
import com.example.sidemanagementbe.chat.dto.SystemMessageType;
import com.example.sidemanagementbe.chat.entity.Chat;
import com.example.sidemanagementbe.chat.repository.ChatArchiveRepository;
import com.example.sidemanagementbe.chat.repository.ChatRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MessageArchiveScheduleTest {
    @Autowired
    private ChatRepository repository;
    @Test
    public void test(){
//        List<Chat> chatList = new ArrayList<>();
//        IntStream.rangeClosed(1,100).forEach(i -> {
//            Chat chat = null;
//            if(i <=50){
//                chat = Chat.builder()
//                        .content("archive content "+i)
//                        .memberId(Long.valueOf(i))
//                        .messageType(SystemMessageType.SEND)
//                        .createdAt(LocalDateTime.now().minusDays(7+i))
//                        .updatedAt(LocalDateTime.now().minusDays(7+i))
//                        .build();
//            }else{
//                Random random = new Random();
//                int randomNumber = random.nextInt(6) + 1;
//
//                chat = Chat.builder()
//                        .content("archive content "+i)
//                        .memberId(Long.valueOf(i))
//                        .messageType(SystemMessageType.SEND)
//                        .createdAt(LocalDateTime.now().minusDays(randomNumber))
//                        .updatedAt(LocalDateTime.now().minusDays(randomNumber))
//                        .build();
//            }
//            chatList.add(chat);
//
//        });

       // repository.saveAll(chatList);
    }

}