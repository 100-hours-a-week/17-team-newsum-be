package com.akatsuki.newsum.common.pagination.querybuilder.base;

import com.akatsuki.newsum.common.pagination.model.cursor.CreatedAtIdCursor;
import com.akatsuki.newsum.common.pagination.model.cursor.Cursor;
import com.akatsuki.newsum.common.pagination.model.cursor.strategy.OrderStrategy;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;

public abstract class CreatedAtIdJpqlPageQueryBuilder extends JpqlPageQueryBuilder<CreatedAtIdCursor> {

	@Override
	protected Class<? extends Cursor> getCursorType() {
		return CreatedAtIdCursor.class;
	}

	@Override
	protected BooleanExpression buildWhereClause(CreatedAtIdCursor cursor) {
		PathBuilder<?> builder = getPathBuilder();
		if (cursor.getStrategy() == OrderStrategy.DESC) {
			return builder.getComparable("createdAt", Comparable.class).loe(cursor.getCreatedAt())
				.or(builder.getComparable("createdAt", Comparable.class).eq(cursor.getCreatedAt())
					.and(builder.getNumber("id", Long.class).lt(cursor.getId())));
		}
		return builder.getComparable("createdAt", Comparable.class).goe(cursor.getCreatedAt())
			.or(builder.getComparable("createdAt", Comparable.class).eq(cursor.getCreatedAt())
				.and(builder.getNumber("id", Long.class).gt(cursor.getId())));
	}

	@Override
	protected OrderSpecifier[] buildOrderByClause(CreatedAtIdCursor cursor) {
		PathBuilder<?> builder = getPathBuilder();
		Order order = cursor.getStrategy() == OrderStrategy.DESC ? Order.DESC : Order.ASC;
		return new OrderSpecifier[] {
			new OrderSpecifier<>(order, builder.getComparable("createdAt", Comparable.class)),
			new OrderSpecifier<>(order, builder.getNumber("id", Long.class))
		};
	}

}
