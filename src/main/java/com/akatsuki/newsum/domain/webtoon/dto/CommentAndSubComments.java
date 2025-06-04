package com.akatsuki.newsum.domain.webtoon.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.akatsuki.newsum.common.pagination.annotation.CursorFieldMapping;

@CursorFieldMapping
public record CommentAndSubComments(
	Long id,
	String content,
	String author,
	String authorProfileImageUrl,
	LocalDateTime createdAt,
	Boolean isLiked,
	Boolean isOwner,
	Long likeCount,
	List<CommentResult> subComments
) {
	public static CommentAndSubComments from(CommentResult parent, List<CommentResult> childComments) {
		return new CommentAndSubComments(
			parent.id(),
			parent.content(),
			parent.author(),
			parent.authorProfileImageUrl(),
			parent.createdAt(),
			parent.isLiked(),
			parent.isOwner(),
			parent.likeCount(),
			childComments
		);
	}
}
