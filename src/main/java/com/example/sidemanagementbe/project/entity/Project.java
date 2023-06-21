package com.example.sidemanagementbe.project.entity;


import com.example.sidemanagementbe.common.entity.BaseEntity;
import com.example.sidemanagementbe.login.entity.Member;
import com.example.sidemanagementbe.team.entity.Team;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "saida_project")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@PrimaryKeyJoinColumn(name = "project_id")
public class Project extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member leader;


    @OneToMany
    @JoinColumn(name = "team_id")
    private List<Team> projects;

}
