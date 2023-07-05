package com.example.sidemanagementbe.home.controller;


import com.example.sidemanagementbe.home.dto.HomeResponse;
import com.example.sidemanagementbe.home.dto.ProjectDto;
import com.example.sidemanagementbe.home.dto.ResumeDto;
import com.example.sidemanagementbe.home.service.HomeService;
import com.example.sidemanagementbe.login.controller.LoginControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.BDDMockito.given;

@AutoConfigureMockMvc
@SpringBootTest
public class HomeControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HomeService homeService;


    @Autowired
    private WebApplicationContext ctx;



    private LoginControllerTest lg;

    @Test
    public void testLoad() throws Exception {
        // Mock HomeResponse 객체 생성
        List<ProjectDto> projectDtos = new ArrayList<>();
        projectDtos.add(ProjectDto.builder().id(1L).build());
        projectDtos.add(ProjectDto.builder().id(2L).build());

        //프로젝트가 2개가 등록되어 있을때

        List<ResumeDto> resumeDtos = new ArrayList<>();
        resumeDtos.add(ResumeDto.builder().id(1L).build());
        resumeDtos.add(ResumeDto.builder().id(2L).build());

        HomeResponse homeResponse = HomeResponse.builder()
                .projects(projectDtos)
                .resumes(resumeDtos)
                .build();

        // Mock HomeService의 반환값 설정
        given(homeService.loadHome()).willReturn(homeResponse);
        // GET /home 요청 수행
        mockMvc.perform(MockMvcRequestBuilders.get("/home")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.projects").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.resumes").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.projects[*].id").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.resumes[*].id").isNotEmpty());

        // 추가적인 Assertion 및 검증 로직을 작성할 수 있습니다.
    }
    @Test
    public void testHomeLoad(){
        List<ProjectDto> projectDtos = new ArrayList<>();
        projectDtos.add(ProjectDto.builder().id(1L).build());
        projectDtos.add(ProjectDto.builder().id(2L).build());

        //프로젝트가 2개가 등록되어 있을때

        List<ResumeDto> resumeDtos = new ArrayList<>();
        resumeDtos.add(ResumeDto.builder().id(1L).build());
        resumeDtos.add(ResumeDto.builder().id(2L).build());

        HomeResponse homeResponse = HomeResponse.builder()
                .projects(projectDtos)
                .resumes(resumeDtos)
                .build();

        assertEquals(2, homeResponse.getProjects().size());
        assertEquals(2, homeResponse.getResumes().size());
    }

}
