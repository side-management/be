package com.example.sidemanagementbe.home.service;

import com.example.sidemanagementbe.home.dto.HomeResponse;
import com.example.sidemanagementbe.home.dto.ProjectDto;
import com.example.sidemanagementbe.home.dto.ResumeDto;
import com.example.sidemanagementbe.home.repository.ProjectRepository;
import com.example.sidemanagementbe.home.repository.ResumeRepository;
import com.example.sidemanagementbe.project.entity.Project;
import com.example.sidemanagementbe.resume.entity.Resume;
import java.util.List;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class HomeService {
    private final ProjectRepository projectRepository;
    private final ResumeRepository resumeRepository;

    //프로젝트 기간
    //프로젝트 상태코드
    //프로젝트 모집기간
    @Transactional
    public HomeResponse loadHome() {
        List<Project> projects = projectRepository.findAll();
        List<Resume> resumes = resumeRepository.findAll();

        List<ProjectDto> projectDtos = convertToProjectDto(projects);
        List<ResumeDto> resumeDtos = convertToResumeDto(resumes);

        HomeResponse homeResponse = new HomeResponse(projectDtos, resumeDtos);

        return homeResponse;
    }


    public List<ProjectDto> convertToProjectDto(List<Project> projects) {
        return projects.stream()
                .map(project -> ProjectDto.builder().id(project.getId()).build())
                .collect(Collectors.toList());
    }

    public List<ResumeDto> convertToResumeDto(List<Resume> resumes) {
        return resumes.stream()
                .map(resume -> ResumeDto.builder().id(resume.getId()).build())
                .collect(Collectors.toList());
    }

}
