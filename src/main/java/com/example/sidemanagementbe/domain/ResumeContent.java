package com.example.sidemanagementbe.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * @author ccik2
 * @class ResumeContent
 * @date 2023-06-05
 **/
@Entity
@Getter
@NoArgsConstructor
@Table(name = "Resume_Content")
public class ResumeContent {
    @Id
    @GeneratedValue
    @Column(name = "resume_id",nullable=false)
    private String resume_id;

    @Column(name = "github_url")
    private String github_url;

    @Column(name = "resume_file_url")
    private String resume_file_url;

    @Column(name = "portfolio_url")
    private String portfolio_url;

    @Builder
    public ResumeContent(String resume_id, String github_url, String resume_file_url, String portfolio_url) {
        this.resume_id = resume_id;
        this.github_url = github_url;
        this.resume_file_url = resume_file_url;
        this.portfolio_url = portfolio_url;
    }
}
