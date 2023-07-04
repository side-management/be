package com.example.sidemanagementbe.login.repository;

import com.example.sidemanagementbe.login.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findByEmail(String email);

    @Query("select m.nickname from Member m where m.id = :id")
    String findNicknameById(@Param("id") Long id);

}
