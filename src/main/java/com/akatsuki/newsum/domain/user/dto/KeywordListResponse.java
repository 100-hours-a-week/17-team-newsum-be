package com.akatsuki.newsum.domain.user.dto;

import java.util.List;

import com.akatsuki.newsum.domain.webtoon.entity.webtoon.KeywordFavorite;

public record KeywordListResponse(
	List<KeywordResponse> keywords
) {
	public static KeywordListResponse from(List<KeywordFavorite> favorites) {
		List<KeywordResponse> keywordDtos = favorites.stream()
			.map(KeywordResponse::from)
			.toList();

		return new KeywordListResponse(keywordDtos);
	}
}
