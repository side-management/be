package com.example.sidemanagementbe.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * @author ccik2
 * @class Team
 * @date 2023-06-05
 **/
@Entity
@Getter
@NoArgsConstructor
@Table(name = "Team")
public class Team {
    @Id
    @GeneratedValue
    @Column(name = "team_id",nullable=false)
    private String team_id;

    @Column(name = "team_name")
    private String team_name;

    @Column(name = "team_description")
    private String team_description;

    @Column(name = "created_at")
    private String created_at;

    @Column(name = "updated_at")
    private String updated_at;

    @Builder
    public Team(String team_id, String team_name, String team_description, String created_at, String updated_at) {
        this.team_id = team_id;
        this.team_name = team_name;
        this.team_description = team_description;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }
}
