package com.akatsuki.newsum.common.pagination.querybuilder;

import com.akatsuki.newsum.common.pagination.model.cursor.Cursor;
import com.akatsuki.newsum.domain.webtoon.entity.comment.entity.QComment;
import com.querydsl.core.types.dsl.BooleanExpression;

public interface CursorQueryBuilder<T extends Cursor> {
	boolean supports(Class<? extends Cursor> cursor);

	BooleanExpression buildBooleanExpression(T cursor, QComment comment);
}
