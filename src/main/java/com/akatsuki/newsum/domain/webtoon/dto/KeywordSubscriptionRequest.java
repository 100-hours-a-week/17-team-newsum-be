package com.akatsuki.newsum.domain.webtoon.dto;

import jakarta.validation.constraints.NotBlank;

public record KeywordSubscriptionRequest(
	@NotBlank
	String keyword
) {
}
