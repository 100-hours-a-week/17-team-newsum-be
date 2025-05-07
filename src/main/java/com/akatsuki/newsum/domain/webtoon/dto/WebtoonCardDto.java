package com.akatsuki.newsum.domain.webtoon.dto;

import java.time.LocalDateTime;

import com.akatsuki.newsum.domain.webtoon.entity.webtoon.Webtoon;

public record WebtoonCardDto(
	Long id,
	String title,
	String thumbnailUrl,
	LocalDateTime createdAt

) {
	public static WebtoonCardDto toDto(Webtoon webtoon) {
		return new WebtoonCardDto(
			webtoon.getId(),
			webtoon.getTitle(),
			webtoon.getThumbnailImageUrl(),
			webtoon.getCreatedAt()
		);
	}
}
