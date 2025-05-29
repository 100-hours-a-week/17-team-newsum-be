package com.akatsuki.newsum.common.pagination.query.base;

import com.akatsuki.newsum.common.pagination.model.cursor.Cursor;
import com.akatsuki.newsum.common.pagination.model.query.QueryEngineType;
import com.akatsuki.newsum.common.pagination.model.query.QueryFragment;
import com.akatsuki.newsum.common.pagination.query.CursorPageQueryBuilder;

public abstract class GenericCursorPageQueryBuilder<T extends Cursor> implements CursorPageQueryBuilder<T> {
	@Override
	public boolean supports(Class<?> domainClass, Class<? extends Cursor> cursorType, QueryEngineType engineType) {
		return engineType.equals(getEngineType())
			&& cursorType.equals(getCursorType())
			&& domainClass.equals(getDomainClass());
	}

	@Override
	public QueryFragment buildQueryFragment(T cursor) {
		return new QueryFragment(
			buildWhereClause(cursor),
			buildOrderByClause(cursor)
		);
	}

	protected abstract QueryEngineType getEngineType();

	protected abstract Class<? extends Cursor> getCursorType();

	protected abstract Class<?> getDomainClass();

	protected abstract Object buildWhereClause(T cursor);

	protected abstract Object buildOrderByClause(T cursor);
}
