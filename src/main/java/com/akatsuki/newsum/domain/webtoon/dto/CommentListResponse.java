package com.akatsuki.newsum.domain.webtoon.dto;

import com.akatsuki.newsum.common.pagination.model.page.CursorPage;
import com.akatsuki.newsum.common.pagination.model.page.PageInfo;
import com.akatsuki.newsum.common.pagination.model.page.PageItemList;

public record CommentListResponse(
	PageItemList<CommentAndSubComments> comments,
	PageInfo pageInfo
) {

	public static CommentListResponse of(CursorPage<CommentAndSubComments> results) {
		return new CommentListResponse(results.getItems(), results.getPageInfo());
	}
}
