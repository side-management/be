package com.example.sidemanagementbe.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * @author ccik2
 * @class TeamProject
 * @date 2023-06-05
 **/
@Entity
@Getter
@NoArgsConstructor
@Table(name = "Team_Project")
public class TeamProject {

    @Id
    @GeneratedValue
    @Column(name = "UniqueId",nullable=false)
    private String uniqueId;

    @Column(name = "project_id")
    private String project_id;

    @Column(name = "team_id")
    private String team_id;

    @Builder
    public TeamProject(String uniqueId, String project_id, String team_id) {
        this.uniqueId = uniqueId;
        this.project_id = project_id;
        this.team_id = team_id;
    }
}
