package com.akatsuki.newsum.common.pagination.deserializer;

import com.akatsuki.newsum.common.pagination.model.cursor.Cursor;

public interface CursorDeserializer<T extends Cursor> {
	boolean supports(Class<? extends Cursor> cursorType);

	T deserialize(String cursor);

	T defaultCursor();
}
