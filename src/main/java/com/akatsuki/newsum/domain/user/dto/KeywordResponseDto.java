package com.akatsuki.newsum.domain.user.dto;

import java.time.LocalDateTime;

import com.akatsuki.newsum.domain.webtoon.entity.webtoon.KeywordFavorite;

public record KeywordResponseDto(
	Long id,
	String content,
	LocalDateTime createAt
) {
	public static KeywordResponseDto from(KeywordFavorite favorites) {
		return new KeywordResponseDto(
			favorites.getKeyword().getId(),
			favorites.getKeyword().getContent(),
			favorites.getCreatedAt()
		);
	}
}
