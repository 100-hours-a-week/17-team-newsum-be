package com.akatsuki.newsum.domain.user.dto;

import jakarta.validation.constraints.NotBlank;

public interface OAuthUserInfo {
	String getEmail();

	String getName();

	String getPicture();

	String getId();

	record KeywordSubscriptionRequest(
		@NotBlank
		String keyword
	) {
	}
}
