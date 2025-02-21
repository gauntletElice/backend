package com.example.elice.common.auth.repository;

import com.example.elice.common.auth.jwt.info.RefreshToken;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRedisRepository extends CrudRepository<RefreshToken, String> {
    RefreshToken findByRefreshToken(String refreshToken);
}
