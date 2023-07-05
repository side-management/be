package com.example.sidemanagementbe.home.repository;

import com.example.sidemanagementbe.project.entity.Project;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findAll();

    Optional<Project> findById(Long id);
}
