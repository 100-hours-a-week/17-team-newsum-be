package com.akatsuki.newsum.common.security;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.akatsuki.newsum.user.domain.Role;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class TokenProvider {

	private final String secretKey;
	private final long accessTokenValidityInMillis;
	private final long refreshTokenValidityInMillis;

	public TokenProvider(
		@Value("${jwt.secret}") String secretKey,
		@Value("${jwt.access-token.expiration}") long accessTokenExpirationSeconds,
		@Value("${jwt.refresh-token.expiration}") long refreshTokenExpirationSeconds
	) {
		this.secretKey = secretKey;
		this.accessTokenValidityInMillis = accessTokenExpirationSeconds * 1000;
		this.refreshTokenValidityInMillis = refreshTokenExpirationSeconds * 1000;
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
