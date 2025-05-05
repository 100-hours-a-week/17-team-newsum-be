package com.akatsuki.newsum.common.pagination.model.page;

import lombok.Getter;

@Getter
public class CursorPage<T> {
	private PageItemList<T> items;
	private PageInfo pageInfo;

	public CursorPage(PageItemList<T> items, PageInfo pageInfo) {
		this.items = items;
		this.pageInfo = pageInfo;
	}

	public static <T> CursorPage<T> empty() {
		return new CursorPage<T>(PageItemList.empty(), PageInfo.empty());
	}
}
