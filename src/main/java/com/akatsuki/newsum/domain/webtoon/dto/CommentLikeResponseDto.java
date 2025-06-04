package com.akatsuki.newsum.domain.webtoon.dto;

public record CommentLikeResponseDto(
	boolean liked,
	long likeCount
) {
}
