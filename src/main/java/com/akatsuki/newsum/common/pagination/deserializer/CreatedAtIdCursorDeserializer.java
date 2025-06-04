package com.akatsuki.newsum.common.pagination.deserializer;

import static com.akatsuki.newsum.common.pagination.model.cursor.strategy.OrderStrategy.*;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.akatsuki.newsum.common.pagination.model.cursor.CreatedAtIdCursor;
import com.akatsuki.newsum.common.pagination.model.cursor.Cursor;
import com.akatsuki.newsum.common.pagination.model.cursor.strategy.OrderStrategy;

@Component
public class CreatedAtIdCursorDeserializer implements CursorDeserializer<CreatedAtIdCursor> {

	private final LocalDateTime minCreatedAt = LocalDateTime.of(1900, 1, 1, 0, 0, 0);
	private final LocalDateTime maxCreatedAt = LocalDateTime.of(2200, 1, 1, 0, 0, 0);

	private final CreatedAtIdCursor minCursor = new CreatedAtIdCursor(minCreatedAt, Long.MIN_VALUE, ASC);
	private final CreatedAtIdCursor maxCursor = new CreatedAtIdCursor(maxCreatedAt, Long.MAX_VALUE, DESC);

	@Override
	public boolean supports(Class<? extends Cursor> cursorType) {
		return cursorType.equals(CreatedAtIdCursor.class);
	}

	@Override
	public CreatedAtIdCursor deserialize(String rawCursor, OrderStrategy strategy) {
		String[] parts = rawCursor.trim().split("_");
		LocalDateTime createdAt = LocalDateTime.parse(parts[0]);
		Long id = Long.parseLong(parts[1]);
		return new CreatedAtIdCursor(createdAt, id, strategy);
	}

	@Override
	public CreatedAtIdCursor defaultCursor() {
		return maxCursor;
	}
}
