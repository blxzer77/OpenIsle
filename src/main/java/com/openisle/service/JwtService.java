package com.openisle.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;


/**
 * JwtService
 *
 * JWT 令牌服务，提供多种场景下的令牌生成与验证能力。
 *
 * 设计要点：
 * - 支持四种不同用途的令牌：普通令牌、注册理由令牌、密码重置令牌、邀请令牌。
 * - 每种令牌使用独立的密钥（secret），提高安全性。
 * - 密钥通过 SHA-256 哈希处理，确保符合 HMAC 算法要求。
 * - 所有令牌共享相同的过期时间配置。
 */
@Service
public class JwtService {
    /** 普通令牌密钥（从配置文件读取） */
    @Value("${app.jwt.secret}")
    private String secret;

    /** 注册理由令牌密钥（从配置文件读取） */
    @Value("${app.jwt.reason-secret}")
    private String reasonSecret;

    /** 密码重置令牌密钥（从配置文件读取） */
    @Value("${app.jwt.reset-secret}")
    private String resetSecret;

    /** 邀请令牌密钥（从配置文件读取） */
    @Value("${app.jwt.invite-secret}")
    private String inviteSecret;

    /** 令牌过期时间（毫秒，从配置文件读取） */
    @Value("${app.jwt.expiration}")
    private long expiration;

    /**
     * 根据提供的密钥字符串生成 HMAC 签名密钥。
     * 使用 SHA-256 对密钥进行哈希处理，确保密钥长度符合 HMAC 要求。
     *
     * @param signSecret 原始密钥字符串
     * @return HMAC 签名密钥
     * @throws IllegalStateException 当 SHA-256 算法不可用时
     */
    private Key getSigningKeyForSecret(String signSecret) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] keyBytes = digest.digest(signSecret.getBytes(StandardCharsets.UTF_8));
            return Keys.hmacShaKeyFor(keyBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 algorithm not available", e);
        }
    }

    /**
     * 生成普通用途的 JWT 令牌。
     * 用于常规的身份认证场景。
     *
     * @param subject 令牌主题（通常是用户ID或用户名）
     * @return JWT 令牌字符串
     */
    public String generateToken(String subject) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);
        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSigningKeyForSecret(secret))
                .compact();
    }

    /**
     * 生成注册理由相关的 JWT 令牌。
     * 用于用户注册时提交理由的验证场景。
     *
     * @param subject 令牌主题（通常是用户ID或用户名）
     * @return JWT 令牌字符串
     */
    public String generateReasonToken(String subject) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);
        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSigningKeyForSecret(reasonSecret))
                .compact();
    }

    /**
     * 生成密码重置相关的 JWT 令牌。
     * 用于用户忘记密码时的重置验证场景。
     *
     * @param subject 令牌主题（通常是用户ID或用户名）
     * @return JWT 令牌字符串
     */
    public String generateResetToken(String subject) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);
        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSigningKeyForSecret(resetSecret))
                .compact();
    }

    /**
     * 生成邀请相关的 JWT 令牌。
     * 用于用户邀请他人注册的验证场景。
     *
     * @param subject 令牌主题（通常是用户ID或用户名）
     * @return JWT 令牌字符串
     */
    public String generateInviteToken(String subject) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);
        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSigningKeyForSecret(inviteSecret))
                .compact();
    }

    /**
     * 验证普通令牌并提取主题信息。
     * 验证令牌的签名、过期时间等，返回令牌中的主题。
     *
     * @param token JWT 令牌字符串
     * @return 令牌主题（通常是用户ID或用户名）
     * @throws io.jsonwebtoken.JwtException 当令牌无效、过期或签名验证失败时
     */
    public String validateAndGetSubject(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKeyForSecret(secret))
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    /**
     * 验证注册理由令牌并提取主题信息。
     *
     * @param token JWT 令牌字符串
     * @return 令牌主题（通常是用户ID或用户名）
     * @throws io.jsonwebtoken.JwtException 当令牌无效、过期或签名验证失败时
     */
    public String validateAndGetSubjectForReason(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKeyForSecret(reasonSecret))
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    /**
     * 验证密码重置令牌并提取主题信息。
     *
     * @param token JWT 令牌字符串
     * @return 令牌主题（通常是用户ID或用户名）
     * @throws io.jsonwebtoken.JwtException 当令牌无效、过期或签名验证失败时
     */
    public String validateAndGetSubjectForReset(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKeyForSecret(resetSecret))
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    /**
     * 验证邀请令牌并提取主题信息。
     *
     * @param token JWT 令牌字符串
     * @return 令牌主题（通常是用户ID或用户名）
     * @throws io.jsonwebtoken.JwtException 当令牌无效、过期或签名验证失败时
     */
    public String validateAndGetSubjectForInvite(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKeyForSecret(inviteSecret))
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }
}
