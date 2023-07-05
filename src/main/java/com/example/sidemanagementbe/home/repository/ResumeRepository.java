package com.example.sidemanagementbe.home.repository;

import com.example.sidemanagementbe.resume.entity.Resume;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ResumeRepository extends JpaRepository<Resume, Long> {
    List<Resume> findAll();

    Optional<Resume> findById(Long id);
}
