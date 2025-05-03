package com.akatsuki.newsum.common.dto.pagination.query.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import com.akatsuki.newsum.common.dto.ErrorCodeAndMessage;
import com.akatsuki.newsum.common.dto.pagination.model.cursor.Cursor;
import com.akatsuki.newsum.common.dto.pagination.query.resolver.CursorQueryResolver;
import com.akatsuki.newsum.common.exception.BusinessException;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CursorQueryResolverMapper {
	private final List<CursorQueryResolver<?>> resolvers;

	public <T extends Cursor> CursorQueryResolver<T> resolveResolver(Cursor cursor) {
		for (CursorQueryResolver<?> resolver : resolvers) {
			if (resolver.supports(cursor)) {
				return (CursorQueryResolver<T>)resolver;
			}
		}
		throw new BusinessException(ErrorCodeAndMessage.CURSOR_WRONG_EXPRESSION);
	}
}
