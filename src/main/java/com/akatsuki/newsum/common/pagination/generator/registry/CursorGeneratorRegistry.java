package com.akatsuki.newsum.common.pagination.generator.registry;

import java.util.List;

import org.springframework.stereotype.Component;

import com.akatsuki.newsum.common.dto.ErrorCodeAndMessage;
import com.akatsuki.newsum.common.exception.BusinessException;
import com.akatsuki.newsum.common.pagination.generator.CursorGenerator;
import com.akatsuki.newsum.common.pagination.model.cursor.Cursor;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CursorGeneratorRegistry {
	private final List<CursorGenerator> generators;

	public CursorGenerator resolve(Cursor cursor) {
		return generators.stream()
			.filter(extractor -> extractor.supports(cursor.getClass()))
			.findFirst()
			.orElseThrow(() -> new BusinessException(ErrorCodeAndMessage.CURSOR_WRONG_EXPRESSION));
	}
}
