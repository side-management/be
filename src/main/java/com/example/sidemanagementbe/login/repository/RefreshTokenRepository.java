package com.example.sidemanagementbe.login.repository;


import com.example.sidemanagementbe.login.dto.RefreshTokenDto;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

@Repository
public class RefreshTokenRepository {

    private final RedisTemplate redisTemplate;

    public RefreshTokenRepository(final RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void save(final RefreshTokenDto refreshToken) {
        try {
            ValueOperations<String, Long> valueOperations = redisTemplate.opsForValue();

            redisTemplate.opsForValue()
                    .set(refreshToken.getRefreshToken(), refreshToken.getMemberId(), 100000L, TimeUnit.SECONDS);
            valueOperations.set(refreshToken.getRefreshToken(), refreshToken.getMemberId());
            redisTemplate.expire(refreshToken.getRefreshToken(), 60L, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void deleteByRefreshToken(final String refreshToken) {

    }

    public Optional<RefreshTokenDto> findById(final String refreshToken) {
        ValueOperations<String, Long> valueOperations = redisTemplate.opsForValue();
        Long memberId = valueOperations.get(refreshToken);

        if (Objects.isNull(memberId)) {
            return Optional.empty();
        }

        return Optional.of(new RefreshTokenDto(refreshToken, memberId));
    }
}