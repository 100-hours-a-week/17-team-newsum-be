package com.akatsuki.newsum.domain.webtoon.dto;

public record WebtoonSlideDto(
	Byte slideSeq,
	String imageUrl,
	String content
) {
}
