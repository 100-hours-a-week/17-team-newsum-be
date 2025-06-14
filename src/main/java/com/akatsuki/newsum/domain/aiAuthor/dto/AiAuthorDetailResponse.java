package com.akatsuki.newsum.domain.aiAuthor.dto;

import java.time.LocalDateTime;
import java.util.List;

public record AiAuthorDetailResponse(
	Long id,
	String name,
	String style,
	String introduction,
	String profileImageUrl,
	LocalDateTime createdAt,
	List<AiAuthorWebtoonResponse> webtoons
) {
}

