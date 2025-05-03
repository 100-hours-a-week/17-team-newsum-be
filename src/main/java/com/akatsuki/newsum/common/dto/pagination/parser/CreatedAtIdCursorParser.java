package com.akatsuki.newsum.common.dto.pagination.parser;

import java.time.LocalDateTime;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

import com.akatsuki.newsum.common.dto.pagination.model.cursor.CreatedAtIdCursor;
import com.akatsuki.newsum.common.dto.pagination.model.cursor.Cursor;

@Component
public class CreatedAtIdCursorParser implements CursorParser {

	private final Pattern CREATED_AT_ID_PATTERN = Pattern.compile("^\\d{4}-\\d{2}-\\d{2}T.*?_\\d+$");

	@Override
	public boolean supports(String rawCursor) {
		return CREATED_AT_ID_PATTERN.matcher(rawCursor).matches();
	}

	@Override
	public Cursor parse(String rawCursor) {
		String[] parts = rawCursor.split("_");
		LocalDateTime createdAt = LocalDateTime.parse(parts[0]);
		Long id = Long.parseLong(parts[1]);
		return new CreatedAtIdCursor(createdAt, id);
	}
}
