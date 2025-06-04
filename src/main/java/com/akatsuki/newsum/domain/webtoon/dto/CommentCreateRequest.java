package com.akatsuki.newsum.domain.webtoon.dto;

public record CommentCreateRequest(
	Long parentId,
	String content
) {
}
