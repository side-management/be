package com.example.sidemanagementbe.team.entity;


import com.example.sidemanagementbe.common.entity.BaseEntity;
import com.example.sidemanagementbe.teammember.entity.TeamMember;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "saida_team")
@PrimaryKeyJoinColumn(name = "team_id")
public class Team extends BaseEntity {
    @OneToMany(mappedBy = "team")
    private List<TeamMember> members = new ArrayList<>();

    public void addMembers(List<TeamMember> members) {
        this.members.addAll(members);
    }


}
