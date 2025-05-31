package com.akatsuki.newsum.common.pagination.model.page;

import com.akatsuki.newsum.common.pagination.model.cursor.Cursor;

public record PageInfo(
	String nextCursor,
	Boolean hasNext
) {
	public static PageInfo empty() {
		return new PageInfo(null, null);
	}

	public static PageInfo end() {
		return new PageInfo(null, false);
	}

	public static PageInfo from(Cursor cursor) {
		return new PageInfo(cursor.getCursor(), true);
	}
}
