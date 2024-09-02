package com.coderio.task.service.impl;

import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import org.springframework.web.util.WebUtils;

import com.coderio.task.repository.RefreshTokenRepository;
import com.coderio.task.repository.entity.RefreshToken;
import com.coderio.task.repository.entity.Role;
import com.coderio.task.repository.entity.User;
import com.coderio.task.service.JwtService;
import com.coderio.task.shared.configuration.MenssageResponse;
import com.coderio.task.shared.configuration.TokenProperties;
import com.coderio.task.shared.exception.TokenRefreshException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class JwtServiceImpl implements JwtService {
    private final TokenProperties tokenProperties;
    private final RefreshTokenRepository refreshTokenRepository;
    private final MenssageResponse menssageResponse;

    @Override
    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    @Override
    public ResponseCookie generateJwtCookie(User user) {
        String jwt = generateToken(Map.of("firstName", user.getFirstName(), "lastName", user.getLastName(), "email",
                user.getEmail(), "roles",
                user.getRole()), user.getUsername());
        if (user.getRole().equals(Role.ADMIN)) {
            return generateCookie(tokenProperties.getJwtCookieName(), jwt, "/users");
        } else {
            return generateCookie(tokenProperties.getJwtCookieName(), jwt, "/tasks");
        }
    }

    @Override
    public RefreshToken createRefreshToken(User user) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setExpiryDate(Instant.now().plusMillis(tokenProperties.getExpirationRefreshTokem()));
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken = refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }

    @Override
    public ResponseCookie generateRefreshJwtCookie(String refreshToken) {
        return generateCookie(tokenProperties.getJwtRefreshCookieName(), refreshToken, "/api/v1/auth/refresh-token");
    }

    @Override
    public String getJwtRefreshFromCookies(HttpServletRequest request) {
        return getCookieValueByName(request, tokenProperties.getJwtRefreshCookieName());
    }

    @Override
    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    @Override
    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new TokenRefreshException(
                    menssageResponse.getResponseDtoByCode(MenssageResponse.OK_RT));
        }
        return token;
    }

    @Override
    public String getJwtFromCookies(HttpServletRequest request) {
        return getCookieValueByName(request, tokenProperties.getJwtCookieName());
    }

    @Override
    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(key()).build().parse(authToken);
            return true;
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }

    @Transactional
    @Override
    public int deleteByUserId(User user) {
        return refreshTokenRepository.deleteByUser(user);
    }

    @Override
    public ResponseCookie getCleanJwtCookie() {
        ResponseCookie cookie = ResponseCookie.from(tokenProperties.getJwtCookieName(), null).path("/tasks").build();
        return cookie;
    }

    @Override
    public ResponseCookie getCleanJwtRefreshCookie() {
        ResponseCookie cookie = ResponseCookie.from(tokenProperties.getJwtRefreshCookieName(), null)
                .path("/api/v1/auth/refresh-token").build();
        return cookie;
    }

    private String getCookieValueByName(HttpServletRequest request, String name) {
        Cookie cookie = WebUtils.getCookie(request, name);
        if (cookie != null) {
            return cookie.getValue();
        } else {
            return null;
        }
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolvers) {
        final Claims claims = extractAllClaims(token);
        return claimsResolvers.apply(claims);
    }

    private String generateToken(Map<String, Object> extraClaims, String username) {

        return Jwts.builder().setClaims(extraClaims).setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date((new Date()).getTime() + tokenProperties.getExpirationToken()))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256).compact();
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token)
                .getBody();
    }

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(tokenProperties.getKey());
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private ResponseCookie generateCookie(String name, String value, String path) {
        ResponseCookie cookie = ResponseCookie.from(name, value).path(path).maxAge(24 * 60 * 60).httpOnly(true).build();
        return cookie;
    }

    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(tokenProperties.getKey()));
    }

}
