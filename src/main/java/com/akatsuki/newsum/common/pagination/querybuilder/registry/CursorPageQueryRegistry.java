package com.akatsuki.newsum.common.pagination.querybuilder.registry;

import java.util.List;

import org.springframework.stereotype.Component;

import com.akatsuki.newsum.common.dto.ErrorCodeAndMessage;
import com.akatsuki.newsum.common.exception.BusinessException;
import com.akatsuki.newsum.common.pagination.model.cursor.Cursor;
import com.akatsuki.newsum.common.pagination.model.query.QueryEngineType;
import com.akatsuki.newsum.common.pagination.querybuilder.CursorPageQueryBuilder;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CursorPageQueryRegistry {
	private final List<CursorPageQueryBuilder<? extends Cursor>> builders;

	@SuppressWarnings("unchecked")
	public <T extends Cursor> CursorPageQueryBuilder<T> resolve(Class<?> domainClass, Cursor cursor,
		QueryEngineType engineType) {
		return (CursorPageQueryBuilder<T>)builders.stream()
			.filter(builder -> builder.supports(domainClass, cursor.getClass(), engineType))
			.findFirst()
			.orElseThrow(() -> new BusinessException(ErrorCodeAndMessage.CURSOR_WRONG_EXPRESSION));
	}
}
