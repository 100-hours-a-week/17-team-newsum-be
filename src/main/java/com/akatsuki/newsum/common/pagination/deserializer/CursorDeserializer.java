package com.akatsuki.newsum.common.pagination.deserializer;

import com.akatsuki.newsum.common.pagination.model.cursor.Cursor;
import com.akatsuki.newsum.common.pagination.model.cursor.strategy.OrderStrategy;

public interface CursorDeserializer<T extends Cursor> {
	boolean supports(Class<? extends Cursor> cursorType);

	T deserialize(String cursor, OrderStrategy strategy);

	T defaultCursor();
}
