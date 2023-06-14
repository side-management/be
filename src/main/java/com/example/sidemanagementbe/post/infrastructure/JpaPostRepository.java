package com.example.sidemanagementbe.post.infrastructure;

import com.example.sidemanagementbe.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaPostRepository extends JpaRepository<Post, Long> {
}
