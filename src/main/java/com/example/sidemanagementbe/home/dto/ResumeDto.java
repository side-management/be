package com.example.sidemanagementbe.home.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public class ResumeDto {

    private Long id;
    //구인중 or 구직중 등등
}
