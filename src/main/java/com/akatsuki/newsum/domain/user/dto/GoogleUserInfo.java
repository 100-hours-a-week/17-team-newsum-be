package com.akatsuki.newsum.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class GoogleUserInfo implements OAuthUserInfo {
	private String email;
	private String name;
	private String picture;
	private String id;

	}
}
