package com.akatsuki.newsum.common.pagination.deserializer.registry;

import java.util.List;

import org.springframework.stereotype.Component;

import com.akatsuki.newsum.common.dto.ErrorCodeAndMessage;
import com.akatsuki.newsum.common.exception.BusinessException;
import com.akatsuki.newsum.common.pagination.deserializer.CursorDeserializer;
import com.akatsuki.newsum.common.pagination.model.cursor.Cursor;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CursorParserRegistry {

	private final List<CursorDeserializer<? extends Cursor>> deserializers;

	public CursorDeserializer<? extends Cursor> resolve(Class<? extends Cursor> cursorType) {
		return deserializers.stream()
			.filter(parser -> parser.supports(cursorType))
			.findFirst()
			.orElseThrow(() -> new BusinessException(ErrorCodeAndMessage.CURSOR_WRONG_EXPRESSION));
	}
}
