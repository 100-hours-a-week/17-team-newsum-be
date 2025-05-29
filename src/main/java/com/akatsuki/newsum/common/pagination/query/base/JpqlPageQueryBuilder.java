package com.akatsuki.newsum.common.pagination.query.base;

import com.akatsuki.newsum.common.pagination.model.cursor.Cursor;
import com.akatsuki.newsum.common.pagination.model.query.QueryEngineType;
import com.querydsl.core.types.dsl.PathBuilder;

public abstract class JpqlPageQueryBuilder<T extends Cursor> extends GenericCursorPageQueryBuilder<T> {

	@Override
	protected QueryEngineType getEngineType() {
		return QueryEngineType.QUERYDSL;
	}

	protected abstract PathBuilder<?> getPathBuilder();
}
