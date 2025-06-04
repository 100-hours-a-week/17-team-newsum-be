package com.akatsuki.newsum.common.security;

import com.akatsuki.newsum.domain.user.dto.OAuthUserInfo;

import lombok.Getter;

@Getter
public class TokenResponse {

	private String accessToken;
	private String refreshToken;
	private OAuthUserInfo userInfo;

	public TokenResponse(String accessToken, String refreshToken, OAuthUserInfo userInfo) {
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
		this.userInfo = userInfo;
	}
}
