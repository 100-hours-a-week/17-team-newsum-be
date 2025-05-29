package com.akatsuki.newsum.domain.webtoon.dto;

import com.akatsuki.newsum.common.pagination.model.page.CursorPage;

public record CommentListResult(
	CursorPage<CommentAndSubComments> cursorPage,
	Long commentCount
) {
}
