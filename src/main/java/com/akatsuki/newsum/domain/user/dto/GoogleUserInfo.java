package com.akatsuki.newsum.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GoogleUserInfo {
	private String email;
	private String name;
	private String picture;
	private String id;
	
}
