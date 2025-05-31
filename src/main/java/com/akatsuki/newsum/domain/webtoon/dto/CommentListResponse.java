package com.akatsuki.newsum.domain.webtoon.dto;

import java.util.List;

import com.akatsuki.newsum.common.pagination.model.page.CursorPage;
import com.akatsuki.newsum.common.pagination.model.page.PageInfo;

public record CommentListResponse(
	List<CommentAndSubComments> comments,
	Long commentCount,
	PageInfo pageInfo
) {

	public static CommentListResponse of(CursorPage<CommentAndSubComments> results, Long commentCount) {
		return new CommentListResponse(results.getItems(), commentCount, results.getPageInfo());
	}
}
