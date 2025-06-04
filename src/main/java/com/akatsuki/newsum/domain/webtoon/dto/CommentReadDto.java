package com.akatsuki.newsum.domain.webtoon.dto;

import java.time.LocalDateTime;

import com.querydsl.core.annotations.QueryProjection;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class CommentReadDto {
	private Long id;
	private Long authorId;
	private String author;
	private String authorProfileImageUrl;
	private String content;
	private Long parentId;
	private Long likeCount;
	private LocalDateTime createdAt;

	@QueryProjection
	public CommentReadDto(Long id, Long authorId, String author, String authorProfileImageUrl, String content,
		Long parentId,
		Long likeCount, LocalDateTime createdAt) {
		this.id = id;
		this.authorId = authorId;
		this.author = author;
		this.authorProfileImageUrl = authorProfileImageUrl;
		this.content = content;
		this.parentId = parentId;
		this.likeCount = likeCount;
		this.createdAt = createdAt;
	}
}
