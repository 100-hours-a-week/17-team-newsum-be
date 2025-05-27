package com.akatsuki.newsum.common.pagination.deserializer;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.akatsuki.newsum.common.pagination.model.cursor.CreatedAtIdCursor;
import com.akatsuki.newsum.common.pagination.model.cursor.Cursor;

@Component
public class CreatedAtIdCursorDeserializer implements CursorDeserializer<CreatedAtIdCursor> {

	private final CreatedAtIdCursor defaultCursor = new CreatedAtIdCursor(LocalDateTime.of(2100, 1, 1, 0, 0, 0),
		Long.MAX_VALUE);

	@Override
	public boolean supports(Class<? extends Cursor> cursorType) {
		return cursorType.equals(CreatedAtIdCursor.class);
	}

	@Override
	public CreatedAtIdCursor deserialize(String rawCursor) {
		String[] parts = rawCursor.trim().split("_");
		LocalDateTime createdAt = LocalDateTime.parse(parts[0]);
		Long id = Long.parseLong(parts[1]);
		return new CreatedAtIdCursor(createdAt, id);
	}

	@Override
	public CreatedAtIdCursor defaultCursor() {
		return defaultCursor;
	}
}
