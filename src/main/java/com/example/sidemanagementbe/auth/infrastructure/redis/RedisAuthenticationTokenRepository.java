package com.example.sidemanagementbe.auth.infrastructure.redis;

import com.example.sidemanagementbe.auth.entity.AuthenticationToken;
import org.springframework.data.repository.CrudRepository;

public interface RedisAuthenticationTokenRepository extends CrudRepository<AuthenticationToken, Long> {
}
