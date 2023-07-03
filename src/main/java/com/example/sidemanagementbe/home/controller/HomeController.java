package com.example.sidemanagementbe.home.controller;

import com.example.sidemanagementbe.home.dto.HomeResponse;
import com.example.sidemanagementbe.home.dto.ProjectDto;
import com.example.sidemanagementbe.home.dto.ResumeDto;
import com.example.sidemanagementbe.home.service.HomeService;
import com.example.sidemanagementbe.project.entity.Project;
import com.example.sidemanagementbe.resume.entity.Resume;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.PermitAll;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@RestController
public class HomeController {


    private final HomeService homeService;
    //채용중인 프로젝트 리스트
    //{리더,}
    @PermitAll
    @GetMapping("/home")
    @Operation(summary = "로그인 성공시 홈화면", description = "프로젝트 리스트와 포르폴리오 리스트를 반환")     //프로젝트 상태값이 필요할거 같아유 ex) 구인중,완료된 프로젝트,진행중인 프로젝트 등
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로드 성공"),
            @ApiResponse(responseCode = "400", description = "로드 실패")
    })
    public ResponseEntity<HomeResponse> load(){
        return ResponseEntity.ok(homeService.loadHome());
    }


    //프로젝트 클릭시
    /*@GetMapping("/home/project_detail/{team_id}")
    @Operation(summary = "프로젝트 상세", description = "프로젝트 정보를 반환")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로드 성공"),
            @ApiResponse(responseCode = "400", description = "로드 실패")
    })
    public ResponseEntity<ProjectDto> ProjectDetail(@PathVariable Long id){
        return "home";
    } */



    //포트폴리오 클릭시
    @GetMapping("/home/resume_detail/{resume_id}")
    @Operation(summary = "이력서 상세", description = "이력서 정보를 반환")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로드 성공"),
            @ApiResponse(responseCode = "400", description = "로드 실패")
    })
    public String ResumeDetail() {
        return "home";
    }


    //프로젝트 변환기
    public List<ProjectDto> convertToProjectDto(List<Project> projects){
        return projects.stream()
                .map(project -> ProjectDto.builder().id(project.getId()).build())
                .collect(Collectors.toList());
    }
    //이력서 변환기
    public List<ResumeDto> convertToResumeDto(List<Resume> resumes){
        return resumes.stream()
                .map(resume -> ResumeDto.builder().id(resume.getId()).build())
                .collect(Collectors.toList());
    }
}
