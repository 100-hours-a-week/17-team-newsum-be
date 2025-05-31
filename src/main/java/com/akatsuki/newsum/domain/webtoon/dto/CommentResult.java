package com.akatsuki.newsum.domain.webtoon.dto;

import java.time.LocalDateTime;

public record CommentResult(
	Long id,
	String content,
	String author,
	String authorProfileImageUrl,
	LocalDateTime createdAt,
	Boolean isLiked,
	Boolean isOwner
) {
	public static CommentResult of(CommentReadDto commentReadDto, Boolean isLiked, Boolean isOwner) {
		return new CommentResult(
			commentReadDto.getId(),
			commentReadDto.getContent(),
			commentReadDto.getAuthor(),
			commentReadDto.getAuthorProfileImageUrl(),
			commentReadDto.getCreatedAt(),
			isLiked,
			isOwner
		);
	}
}
