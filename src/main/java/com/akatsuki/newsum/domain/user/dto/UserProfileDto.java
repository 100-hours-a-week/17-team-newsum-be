package com.akatsuki.newsum.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserProfileDto {
	private String email;
	private String nickname;
	private String profileImageUrl;
}
