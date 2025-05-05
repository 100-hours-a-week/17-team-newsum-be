package com.akatsuki.newsum.common.pagination.querybuilder;

import org.springframework.stereotype.Component;

import com.akatsuki.newsum.common.pagination.model.cursor.CreatedAtIdCursor;
import com.akatsuki.newsum.common.pagination.model.cursor.Cursor;
import com.akatsuki.newsum.domain.webtoon.entity.comment.entity.QComment;
import com.querydsl.core.types.dsl.BooleanExpression;

@Component
public class CreatedAtIdCursorQueryBuilder implements CursorQueryBuilder<CreatedAtIdCursor> {

	@Override
	public boolean supports(Class<? extends Cursor> cursorType) {
		return cursorType.equals(CreatedAtIdCursor.class);
	}

	@Override
	public BooleanExpression buildBooleanExpression(CreatedAtIdCursor cursor, QComment comment) {
		return comment.createdAt.gt(cursor.getCreatedAt())
			.or(comment.createdAt.eq(cursor.getCreatedAt()).and(comment.id.goe(cursor.getId())));
	}
}
