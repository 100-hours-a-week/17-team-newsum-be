package com.akatsuki.newsum.domain.webtoon.dto;

import java.util.List;

import com.akatsuki.newsum.common.pagination.model.page.CursorPage;
import com.akatsuki.newsum.common.pagination.model.page.PageInfo;

public record WebtoonListResponse(
	List<WebtoonCardDto> webtoons,
	PageInfo pageInfo
) {

	public static WebtoonListResponse of(CursorPage<WebtoonCardDto> cursorPage) {
		return new WebtoonListResponse(cursorPage.getItems(), cursorPage.getPageInfo());
	}
}
