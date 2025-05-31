package com.akatsuki.newsum.common.security;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.akatsuki.newsum.common.dto.ErrorCodeAndMessage;
import com.akatsuki.newsum.common.exception.UnauthorizedException;
import com.akatsuki.newsum.domain.user.entity.UserRole;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;

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

	public String createAccessToken(Long userId, String email, UserRole role) {
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

	public String getPrincipal(String token) {
		try {
			return Jwts.parser()
				.setSigningKey(secretKey.getBytes())
				.parseClaimsJws(token)
				.getBody()
				.getSubject();
		} catch (JwtException | IllegalArgumentException e) {
			throw new UnauthorizedException(ErrorCodeAndMessage.UNAUTHORIZED);
		}
	}

	public List<String> getRoles(String token) {
		try {
			return Jwts.parser()
				.setSigningKey(secretKey.getBytes())
				.parseClaimsJws(token)
				.getBody()
				.get("roles", List.class);
		} catch (JwtException | IllegalArgumentException e) {
			throw new UnauthorizedException(ErrorCodeAndMessage.UNAUTHORIZED);
		}
	}

	public Long getUserIdFromToken(String token) {
		try {
			return Jwts.parser()
				.setSigningKey(secretKey.getBytes())
				.parseClaimsJws(token)
				.getBody()
				.get("userId", Integer.class)  // DB에서 userId가 Long이면 Long.class 써도 됩니다.
				.longValue();
		} catch (JwtException | IllegalArgumentException e) {
			throw new UnauthorizedException(ErrorCodeAndMessage.UNAUTHORIZED);
		}
	}

	public boolean validateToken(String token) {
		try {
			Jws<Claims> claims = Jwts.parser()
				.setSigningKey(secretKey.getBytes())
				.parseClaimsJws(token);

			return !claims.getBody().getExpiration().before(new Date());
		} catch (JwtException | IllegalArgumentException e) {
			return false;
		}
	}

	public String resolveToken(HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization");
		if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7); // "Bearer " 제거 후 토큰 반환
		}
		return null;
	}
}
