package com.akatsuki.newsum.domain.webtoon.dto;

public record CommentLikeStatusDto(
	boolean liked,
	long likeCount
) {
}
