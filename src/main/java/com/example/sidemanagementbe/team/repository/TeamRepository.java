package com.example.sidemanagementbe.team.repository;

import com.example.sidemanagementbe.team.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Long> {
}
