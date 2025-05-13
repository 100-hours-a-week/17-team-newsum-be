package com.akatsuki.newsum.domain.webtoon.dto;

import java.util.List;

public record CommentListResult(
	List<CommentAndSubComments> comments,
	Long commentCount
) {
}
