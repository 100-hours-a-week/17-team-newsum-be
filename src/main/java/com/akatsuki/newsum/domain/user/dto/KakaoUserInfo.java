package com.akatsuki.newsum.domain.user.dto;

import java.util.Map;

import lombok.Getter;

@Getter
public class KakaoUserInfo implements OAuthUserInfo {
	private final String email;
	private final String name;
	private final String picture;
	private final String id;

	public KakaoUserInfo(Map<String, Object> attributes) {
		this.id = String.valueOf(attributes.get("id"));

		Map<String, Object> kakaoAccount = (Map<String, Object>)attributes.get("kakao_account");
		Map<String, Object> properties = (Map<String, Object>)attributes.get("properties");

		this.email = (String)kakaoAccount.get("email");
		this.name = (String)properties.get("nickname");
		this.picture = (String)properties.get("profile_image");
	}
}
