package com.example.sidemanagementbe.login.repository;

import com.example.sidemanagementbe.login.dto.RefreshTokenDto;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RefreshTokenRepositoryTest {
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Test
    @DisplayName("Redis Repository 테스트")
    public void redisRepositoryTest() throws Exception {
        //given
        String sampleRefreshToken = "sdf234ksdcfsdkj23sccxv";
        Long sampleId = 23253L;
        RefreshTokenDto refreshTokenDto = new RefreshTokenDto(sampleRefreshToken, sampleId);
        refreshTokenRepository.save(refreshTokenDto);

        //when
        Optional<RefreshTokenDto> result = refreshTokenRepository.findById("sdf234ksdcfsdkj23sccxv");
        RefreshTokenDto dto = result.get();

        //then
        Assertions.assertEquals(dto.getRefreshToken(), "sdf234ksdcfsdkj23sccxv");
        Assertions.assertEquals(dto.getMemberId(), 23253L);

    }

}