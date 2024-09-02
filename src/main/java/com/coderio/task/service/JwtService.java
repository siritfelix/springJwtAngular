package com.coderio.task.service;

import java.util.Optional;

import org.springframework.http.ResponseCookie;

import com.coderio.task.repository.entity.RefreshToken;
import com.coderio.task.repository.entity.User;

import jakarta.servlet.http.HttpServletRequest;

public interface JwtService {
    String extractUserName(String token);

    ResponseCookie generateJwtCookie(User user);

    RefreshToken createRefreshToken(User user);

    ResponseCookie generateRefreshJwtCookie(String refreshToken);

    String getJwtRefreshFromCookies(HttpServletRequest request);

    Optional<RefreshToken> findByToken(String token);

    RefreshToken verifyExpiration(RefreshToken token);

    String getJwtFromCookies(HttpServletRequest request);

    boolean validateJwtToken(String authToken);

    int deleteByUserId(User user);

    ResponseCookie getCleanJwtCookie();

    ResponseCookie getCleanJwtRefreshCookie();
}
