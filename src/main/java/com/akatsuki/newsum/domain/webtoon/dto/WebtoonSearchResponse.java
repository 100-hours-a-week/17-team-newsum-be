package com.akatsuki.newsum.domain.webtoon.dto;

import java.util.List;

import com.akatsuki.newsum.common.pagination.model.page.CursorPage;
import com.akatsuki.newsum.common.pagination.model.page.PageInfo;

public record WebtoonSearchResponse(
	List<WebtoonCardDto> webtoons,
	PageInfo pageInfo
) {
	public static WebtoonSearchResponse of(CursorPage<WebtoonCardDto> page) {
		return new WebtoonSearchResponse(page.getItems(), page.getPageInfo());
	}
}
