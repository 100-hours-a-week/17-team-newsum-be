package com.akatsuki.newsum.domain.webtoon.dto;

import java.util.List;

public record WebtoonResponse(
	Long id,
	String thumbnailImageUrl,
	String title,
	List<WebtoonSlideDto> slides,
	AiAuthorInfoDto author,
	boolean isLiked,
	boolean isBookmarked,
	Long likeCount,
	Long viewCount
) {
}
