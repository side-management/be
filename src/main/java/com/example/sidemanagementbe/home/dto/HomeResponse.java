package com.example.sidemanagementbe.home.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
@AllArgsConstructor
public class HomeResponse {
    @Schema(description = "프로젝트 ID", nullable = false, example = "{1,2,3,4,5,6}")
    private List<ProjectDto> projects;
    @Schema(description = "이력서 ID", nullable = false, example = "{1,2,3,4}")
    private List<ResumeDto> resumes;
}
