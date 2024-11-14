package com.app.global.jwt.service;

import com.app.domain.member.constant.Role;
import com.app.global.error.ErrorCode;
import com.app.global.error.exception.AuthenticationException;
import com.app.global.jwt.constant.GrantType;
import com.app.global.jwt.constant.TokenType;
import com.app.global.jwt.dto.JwtTokenDto;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Slf4j
@RequiredArgsConstructor
public class TokenManager {

    private final String accessTokenExpirationTime;
    private final String refreshTokenExpirationTime;
    private final String tokenSecret;

    // JWT 토큰 생성 메서드 (액세스 토큰 및 리프레시 토큰 생성)
    public JwtTokenDto createJwtTokenDto(Long memberId, Role role) {
        Date accessTokenExpireTime = createAccessTokenExpireTime();
        Date refreshTokenExpireTime = createRefreshTokenExpireTime();

        String accessToken = createAccessToken(memberId, role, accessTokenExpireTime);
        String refreshToken = createRefreshToken(memberId, refreshTokenExpireTime);
        return JwtTokenDto.builder()
                .grantType(GrantType.BEARER.getType())
                .accessToken(accessToken)
                .accessTokenExpireTime(accessTokenExpireTime)
                .refreshToken(refreshToken)
                .refreshTokenExpireTime(refreshTokenExpireTime)
                .build(); //최종 객체 생성
    }

    // 액세스 토큰 만료 시간 생성
    public Date createAccessTokenExpireTime() {
        return new Date(System.currentTimeMillis() + Long.parseLong(accessTokenExpirationTime));
    }

    // 리프레시 토큰 만료 시간 생성
    public Date createRefreshTokenExpireTime() {
        return new Date(System.currentTimeMillis() + Long.parseLong(refreshTokenExpirationTime));
    }

    // 액세스 토큰 생성
    public String createAccessToken(Long memberId, Role role, Date expirationTime) {
        Key key = Keys.hmacShaKeyFor(tokenSecret.getBytes(StandardCharsets.UTF_8)); // SecretKey 생성
        String accessToken = Jwts.builder()
                .setSubject(TokenType.ACCESS.name())    // 토큰 제목 설정
                .setIssuedAt(new Date())                // 토큰 발급 시간 설정
                .setExpiration(expirationTime)          // 토큰 만료 시간 설정
                .claim("memberId", memberId)           // 회원 아이디 추가
                .claim("role", role)                   // 유저 역할 추가
                .signWith(key, SignatureAlgorithm.HS512) // SecretKey와 서명 알고리즘 설정
                .setHeaderParam("typ", "JWT")
                .compact();
        return accessToken;
    }

    // 리프레시 토큰 생성
    public String createRefreshToken(Long memberId, Date expirationTime) {
        Key key = Keys.hmacShaKeyFor(tokenSecret.getBytes(StandardCharsets.UTF_8)); // SecretKey 생성
        String refreshToken = Jwts.builder()
                .setSubject(TokenType.REFRESH.name())   // 토큰 제목 설정
                .setIssuedAt(new Date())                // 토큰 발급 시간 설정
                .setExpiration(expirationTime)          // 토큰 만료 시간 설정
                .claim("memberId", memberId)           // 회원 아이디 추가
                .signWith(key, SignatureAlgorithm.HS512) // SecretKey와 서명 알고리즘 설정
                .setHeaderParam("typ", "JWT")
                .compact();
        return refreshToken;
    }

    // 토큰 유효성 검사
    public void validateToken(String token) {
        try {
            Key key = Keys.hmacShaKeyFor(tokenSecret.getBytes(StandardCharsets.UTF_8)); // SecretKey 생성
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token); // 토큰 파싱 및 유효성 검사
        } catch (ExpiredJwtException e) {
            log.info("token 만료", e);
            throw new AuthenticationException(ErrorCode.TOKEN_EXPIRED);
        } catch (Exception e) {
            log.info("유효하지 않은 token", e);
            throw new AuthenticationException(ErrorCode.NOT_VALID_TOKEN);
        }
    }

    // 토큰에서 Claims(클레임) 추출
    public Claims getTokenClaims(String token) {
        Claims claims;
        try {
            Key key = Keys.hmacShaKeyFor(tokenSecret.getBytes(StandardCharsets.UTF_8)); // SecretKey 생성
            claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody(); // 토큰 파싱 및 클레임 추출
        } catch (Exception e) {
            log.info("유효하지 않은 token", e);
            throw new AuthenticationException(ErrorCode.NOT_VALID_TOKEN);
        }
        return claims;
    }
}
