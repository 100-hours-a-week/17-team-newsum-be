package com.akatsuki.newsum.common.dto.pagination.parser;

import com.akatsuki.newsum.common.dto.pagination.model.cursor.Cursor;

public interface CursorParser {
	boolean supports(String cursor);

	Cursor parse(String cursor);
}
