package com.example.sidemanagementbe.testdata;

import com.example.sidemanagementbe.chat.dto.SystemMessageType;
import com.example.sidemanagementbe.chat.entity.Chat;
import com.example.sidemanagementbe.chat.repository.ChatRepository;
import com.example.sidemanagementbe.login.entity.Gender;
import com.example.sidemanagementbe.login.entity.Member;
import com.example.sidemanagementbe.login.entity.MemberRole;
import com.example.sidemanagementbe.login.repository.MemberRepository;
import com.example.sidemanagementbe.team.entity.Team;
import com.example.sidemanagementbe.team.repository.TeamRepository;
import com.example.sidemanagementbe.teammember.entity.TeamMember;
import com.example.sidemanagementbe.teammember.repository.TeamMemberRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;


@Profile("local")
@RequiredArgsConstructor
@Component
public class TestData {
    private final MemberRepository memberRepository;
    private final ChatRepository chatRepository;

    private final TeamRepository teamRepository;

    private final TeamMemberRepository teamMemberRepository;


    @Bean
    public ApplicationRunner runner() {
        return new ApplicationRunner() {
            @Override
            public void run(ApplicationArguments args) throws Exception {
                Member member1 = Member.createMember("nick1111@naver.com", "텐시1", Gender.MAN, "kakao", "1324",
                        "localhost:8080/imageUrl1", MemberRole.USER);
                Member member2 = Member.createMember("nick2222@naver.com", "텐시2", Gender.WOMAN, "kakao", "2435",
                        "localhost:1010/imageUrl2", MemberRole.USER);
                Member member3 = Member.createMember("nick3333@naver.com", "텐시3", Gender.MAN, "kakao", "3546",
                        "localhost:2020/imageUrl3", MemberRole.USER);
                Member member4 = Member.createMember("nick4444@naver.com", "텐시4", Gender.WOMAN, "kakao", "4657",
                        "localhost:3040/imageUrl4", MemberRole.USER);
                Member member5 = Member.createMember("nick5555@naver.com", "텐시5", Gender.MAN, "kakao", "5768",
                        "localhost:5050/imageUrl5", MemberRole.USER);

                memberRepository.save(member1);
                memberRepository.save(member2);
                memberRepository.save(member3);
                memberRepository.save(member4);
                memberRepository.save(member5);

                Team team1 = new Team();
                Team team2 = new Team();
                Team team3 = new Team();

                TeamMember teamMember1 = TeamMember.builder()
                        .member(member1)
                        .team(team1)
                        .build();
                TeamMember teamMember2 = TeamMember.builder()
                        .member(member2)
                        .team(team1)
                        .build();
                TeamMember teamMember3 = TeamMember.builder()
                        .member(member3)
                        .team(team2)
                        .build();
                TeamMember teamMember4 = TeamMember.builder()
                        .member(member4)
                        .team(team2)
                        .build();
                TeamMember teamMember5 = TeamMember.builder()
                        .member(member5)
                        .team(team3)
                        .build();

                List<TeamMember> list1 = Arrays.asList(teamMember1, teamMember2);
                List<TeamMember> list2 = Arrays.asList(teamMember3, teamMember4);
                List<TeamMember> list3 = Collections.singletonList(teamMember5);

                team1.addMembers(list1);
                team2.addMembers(list2);
                team3.addMembers(list3);

                teamRepository.save(team1);
                teamRepository.save(team2);
                teamRepository.save(team3);

                teamMemberRepository.save(teamMember1);
                teamMemberRepository.save(teamMember2);
                teamMemberRepository.save(teamMember3);
                teamMemberRepository.save(teamMember4);
                teamMemberRepository.save(teamMember5);

                List<Chat> chatList = new ArrayList<>();
                IntStream.rangeClosed(1, 100).forEach(i -> {
                    Chat chat = null;
                    if (i <= 50) {
                        chat = Chat.builder()
                                .content("archive content " + i)
                                .teamId(Long.valueOf(i))
                                .memberId(Long.valueOf(i))
                                .messageType(SystemMessageType.SEND)
                                .createdAt(LocalDateTime.now().minusDays(7 + i))
                                .updatedAt(LocalDateTime.now().minusDays(7 + i))
                                .build();
                    } else {
                        Random random = new Random();
                        int randomNumber = random.nextInt(6) + 1;

                        chat = Chat.builder()
                                .content("archive content " + i)
                                .teamId(Long.valueOf(i))
                                .memberId(Long.valueOf(i))
                                .messageType(SystemMessageType.SEND)
                                .createdAt(LocalDateTime.now().minusDays(randomNumber))
                                .updatedAt(LocalDateTime.now().minusDays(randomNumber))
                                .build();
                    }
                    chatList.add(chat);

                });

                chatRepository.saveAll(chatList);


            }
        };
    }
}
