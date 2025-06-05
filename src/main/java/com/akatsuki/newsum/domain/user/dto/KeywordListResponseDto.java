package com.akatsuki.newsum.domain.user.dto;

import java.util.List;

import com.akatsuki.newsum.domain.webtoon.entity.webtoon.KeywordFavorite;

public record KeywordListResponseDto(
	List<KeywordResponseDto> keywords
) {
	public static KeywordListResponseDto from(List<KeywordFavorite> favorites) {
		List<KeywordResponseDto> keywordDtos = favorites.stream()
			.map(KeywordResponseDto::from)
			.toList();

		return new KeywordListResponseDto(keywordDtos);
	}
}
