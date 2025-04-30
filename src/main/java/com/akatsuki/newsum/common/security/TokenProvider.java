package com.akatsuki.newsum.common.security;

import com.akatsuki.newsum.user.domain.Role;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class TokenProvider {

    private final String secretKey;
    private final long accessTokenValidityInMillis;
    private final long refreshTokenValidityInMillis = 1000L * 60 * 60 * 24 * 7; // 7일

    public TokenProvider(
            @Value("${jwt.secret}") String secretKey,
            @Value("${jwt.expiration}") long expirationSeconds
    ) {
        this.secretKey = secretKey;
        this.accessTokenValidityInMillis = expirationSeconds * 1000;
    }

    public String createAccessToken(Long userId, String email, Role role) {
        Date now = new Date();
        return Jwts.builder()
                .setSubject(email)
                .claim("userId", userId)
                .claim("role", role.name())
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + accessTokenValidityInMillis))
                .signWith(SignatureAlgorithm.HS256, secretKey.getBytes())
                .compact();
    }

    public String createRefreshToken() {
        Date now = new Date();
        return Jwts.builder()
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + refreshTokenValidityInMillis))
                .signWith(SignatureAlgorithm.HS256, secretKey.getBytes())
                .compact();
    }
}
