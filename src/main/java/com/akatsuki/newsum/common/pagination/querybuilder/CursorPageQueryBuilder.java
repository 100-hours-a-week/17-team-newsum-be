package com.akatsuki.newsum.common.pagination.querybuilder;

import com.akatsuki.newsum.common.pagination.model.cursor.Cursor;
import com.akatsuki.newsum.common.pagination.model.query.QueryEngineType;
import com.akatsuki.newsum.common.pagination.model.query.QueryFragment;

public interface CursorPageQueryBuilder<T extends Cursor> {
	boolean supports(Class<?> domainClass, Class<? extends Cursor> cursorType, QueryEngineType engineType);

	QueryFragment buildQueryFragment(T cursor);
}
