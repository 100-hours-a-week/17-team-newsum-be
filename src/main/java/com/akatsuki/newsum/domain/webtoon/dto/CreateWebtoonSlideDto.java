package com.akatsuki.newsum.domain.webtoon.dto;

public record CreateWebtoonSlideDto(
	Long slideSequence,
	String slideImageUrl,
	String content
) {
}
