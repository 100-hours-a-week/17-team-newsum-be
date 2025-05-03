package com.akatsuki.newsum.common.dto.pagination.creator;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Component;

import com.akatsuki.newsum.common.dto.ErrorCodeAndMessage;
import com.akatsuki.newsum.common.dto.pagination.model.cursor.CreatedAtIdCursor;
import com.akatsuki.newsum.common.dto.pagination.model.cursor.Cursor;
import com.akatsuki.newsum.common.dto.pagination.parser.CursorParser;
import com.akatsuki.newsum.common.exception.BusinessException;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CursorCreator {

	private final List<CursorParser> cursorParsers;

	private final Cursor DEFAULT_CURSOR = new CreatedAtIdCursor(LocalDateTime.of(2000, 1, 1, 0, 0, 0), 0L);

	public Cursor createFromRawCursor(String rawCursor) {
		if (rawCursor == null || rawCursor.isBlank()) {
			return DEFAULT_CURSOR;
		}

		return cursorParsers.stream()
			.filter(parser -> parser.supports(rawCursor))
			.findFirst()
			.map(parser -> parser.parse(rawCursor))
			.orElseThrow(() -> new BusinessException(ErrorCodeAndMessage.CURSOR_WRONG_EXPRESSION));
	}
}
