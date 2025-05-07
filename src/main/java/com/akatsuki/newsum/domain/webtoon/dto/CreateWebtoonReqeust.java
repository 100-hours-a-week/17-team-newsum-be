package com.akatsuki.newsum.domain.webtoon.dto;

import java.util.List;

public record CreateWebtoonReqeust(
	Long aiAuthorId,
	String category,
	String title,
	String content,
	String thumbnailImageUrl,
	List<WebtoonSlideDto> slides,
	List<NewsSourceDto> sourceNews
) {
}
