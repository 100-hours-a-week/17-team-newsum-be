package com.akatsuki.newsum.common.pagination.generator;

import com.akatsuki.newsum.common.pagination.model.cursor.Cursor;

public interface CursorGenerator {
	boolean supports(Class<? extends Cursor> cursorType);

	<T> Cursor generate(T element);
}
