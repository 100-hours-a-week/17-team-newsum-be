package com.akatsuki.newsum.domain.user.dto;

import java.time.LocalDateTime;

import com.akatsuki.newsum.domain.webtoon.entity.webtoon.KeywordFavorite;

public record KeywordResponse(
	Long id,
	String content,
	LocalDateTime createAt
) {
	public static KeywordResponse from(KeywordFavorite favorites) {
		return new KeywordResponse(
			favorites.getKeyword().getId(),
			favorites.getKeyword().getContent(),
			favorites.getCreatedAt()
		);
	}
}
