package com.akatsuki.newsum.common.dto.pagination.query.resolver;

import org.springframework.stereotype.Component;

import com.akatsuki.newsum.common.dto.pagination.model.cursor.CreatedAtIdCursor;
import com.akatsuki.newsum.common.dto.pagination.model.cursor.Cursor;
import com.akatsuki.newsum.domain.webtoon.entity.comment.entity.QComment;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;

@Component
public class CreatedAtIdCursorQueryResolver implements CursorQueryResolver<CreatedAtIdCursor> {

	@Override
	public boolean supports(Cursor cursor) {
		return cursor instanceof CreatedAtIdCursor;
	}

	@Override
	public BooleanExpression resolveBooleanExpression(CreatedAtIdCursor cursor, QComment comment) {
		return comment.createdAt.gt(cursor.getCreatedAt())
			.or(comment.createdAt.eq(cursor.getCreatedAt())
				.and(comment.id.gt(cursor.getId())));
	}

	@Override
	public OrderSpecifier<?>[] resolveOrderSpecifiers(QComment comment) {
		return new OrderSpecifier[] {
			comment.createdAt.asc(),
			comment.id.asc()
		};
	}
}
