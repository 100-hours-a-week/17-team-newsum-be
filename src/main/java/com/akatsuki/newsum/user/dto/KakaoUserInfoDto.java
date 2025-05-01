package com.akatsuki.newsum.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class KakaoUserInfoDto {
	private String email;
	private String nickname;
	private String profileImageUrl;
	private String socialId;

	public KakaoUserInfoDto(String email, String nickname, String profileImageUrl, String socialId) {
		this.email = email;
		this.nickname = nickname;
		this.profileImageUrl = profileImageUrl;
		this.socialId = socialId;
	}
}

