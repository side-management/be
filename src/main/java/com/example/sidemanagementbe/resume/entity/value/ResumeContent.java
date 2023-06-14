package com.example.sidemanagementbe.resume.entity.value;


import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class ResumeContent {
    @Column(name = "github_url")
    private String githubUrl;

    @Column(name = "file_url")
    private String fileUrl;

    @Column(name = "portfolio_url")
    private String portfolioUrl;
}
