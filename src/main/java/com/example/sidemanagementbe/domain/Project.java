package com.example.sidemanagementbe.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * @author ccik2
 * @class Project
 * @date 2023-06-05
 **/
@Entity
@Getter
@NoArgsConstructor
@Table(name = "Project")
public class Project {

    @Id
    @GeneratedValue
    @Column(name = "project_id",nullable=false)
    private String project_id;

    @Column(name = "project_name")
    private String project_name;

    @Column(name = "project_description")
    private String project_description;

    @Column(name = "start_date")
    private String start_date;

    @Column(name = "end_date")
    private String end_date;

    @Column(name = "status")
    private String budget;

    @Column(name = "created_at")
    private String created_at;

    @Column(name = "updated_at")
    private String updated_at;

    @Builder
    public Project(String project_id, String project_name, String project_description, String start_date, String end_date, String budget, String created_at, String updated_at) {
        this.project_id = project_id;
        this.project_name = project_name;
        this.project_description = project_description;
        this.start_date = start_date;
        this.end_date = end_date;
        this.budget = budget;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }
}
