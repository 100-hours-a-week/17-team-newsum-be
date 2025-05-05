package com.akatsuki.newsum.common.pagination.querybuilder.registry;

import java.util.List;

import org.springframework.stereotype.Component;

import com.akatsuki.newsum.common.dto.ErrorCodeAndMessage;
import com.akatsuki.newsum.common.exception.BusinessException;
import com.akatsuki.newsum.common.pagination.model.cursor.Cursor;
import com.akatsuki.newsum.common.pagination.querybuilder.CursorQueryBuilder;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CursorQueryRegistry {
	private final List<CursorQueryBuilder<? extends Cursor>> builders;

	@SuppressWarnings("unchecked")
	public <T extends Cursor> CursorQueryBuilder<T> resolve(Cursor cursor) {
		return (CursorQueryBuilder<T>)builders.stream()
			.filter(builder -> builder.supports(cursor.getClass()))
			.findFirst()
			.orElseThrow(() -> new BusinessException(ErrorCodeAndMessage.CURSOR_WRONG_EXPRESSION));
	}
}
