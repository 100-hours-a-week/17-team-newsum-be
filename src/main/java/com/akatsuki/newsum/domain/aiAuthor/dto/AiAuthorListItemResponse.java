package com.akatsuki.newsum.domain.aiAuthor.dto;

import java.time.LocalDateTime;

public record AiAuthorListItemResponse(
	Long id,
	String name,
	String profileImageUrl,
	LocalDateTime createdAt,
	boolean subscribed
) {
}
