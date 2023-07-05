package com.example.sidemanagementbe.home.repository;

import com.example.sidemanagementbe.resume.entity.Resume;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResumeRepository extends JpaRepository<Resume, Long> {
    List<Resume> findAll();

    Optional<Resume> findById(Long id);
}
