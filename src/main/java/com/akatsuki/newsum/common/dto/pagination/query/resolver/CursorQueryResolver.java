package com.akatsuki.newsum.common.dto.pagination.query.resolver;

import com.akatsuki.newsum.common.dto.pagination.model.cursor.Cursor;
import com.akatsuki.newsum.domain.webtoon.entity.comment.entity.QComment;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;

public interface CursorQueryResolver<C extends Cursor> {
	boolean supports(Cursor cursor);

	BooleanExpression resolveBooleanExpression(C cursor, QComment comment);

	OrderSpecifier<?>[] resolveOrderSpecifiers(QComment root);
}
