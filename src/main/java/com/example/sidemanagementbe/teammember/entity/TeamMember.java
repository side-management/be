package com.example.sidemanagementbe.teammember.entity;


import com.example.sidemanagementbe.common.entity.BaseEntity;
import com.example.sidemanagementbe.login.entity.Member;
import com.example.sidemanagementbe.team.entity.Team;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "saida_team_member")
@PrimaryKeyJoinColumn(name = "team_member_id")
public class TeamMember extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
}
