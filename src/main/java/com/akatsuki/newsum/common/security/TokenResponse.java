package com.akatsuki.newsum.common.security;

import com.akatsuki.newsum.domain.user.dto.GoogleUserInfo;

import lombok.Getter;

@Getter
public class TokenResponse {

	private String accessToken;
	private String refreshToken;
	private GoogleUserInfo userInfo;

	public TokenResponse(String accessToken, String refreshToken, GoogleUserInfo userInfo) {
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
		this.userInfo = userInfo;
	}
}
