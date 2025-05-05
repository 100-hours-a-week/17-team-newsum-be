package com.akatsuki.newsum.common.security;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtTokenUtil {

	private final TokenProvider tokenProvider;

	private static final String BEARER_PREFIX = "Bearer ";

	public static String parseBearerToken(String bearerToken) {
		if (bearerToken.startsWith(BEARER_PREFIX)) {
			return bearerToken.substring(BEARER_PREFIX.length());
		}
		return null;
	}
}
