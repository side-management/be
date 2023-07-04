package com.example.sidemanagementbe.teammember.repository;

import com.example.sidemanagementbe.teammember.entity.TeamMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TeamMemberRepository extends JpaRepository<TeamMember, Long> {
    @Query("select tm.team.id from TeamMember tm where tm.team.id = :tId and tm.member.id = :mId")
    Long findTeamIdByMemberId(@Param("tId") Long teamId, @Param("mId") Long memberId);
}
