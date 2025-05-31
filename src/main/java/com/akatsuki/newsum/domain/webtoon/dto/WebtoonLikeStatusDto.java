package com.akatsuki.newsum.domain.webtoon.dto;

public record WebtoonLikeStatusDto(
	boolean liked,
	long likeCount
) {
}
