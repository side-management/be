package com.example.sidemanagementbe.team.entity;


import com.example.sidemanagementbe.common.entity.BaseEntity;
import com.example.sidemanagementbe.teammember.entity.TeamMember;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "saida_team")
@PrimaryKeyJoinColumn(name = "team_id")
public class Team extends BaseEntity {


    @OneToMany(mappedBy = "team")
    private List<TeamMember> members;

}
