package com.akatsuki.newsum.common.pagination.generator;

import static com.akatsuki.newsum.common.pagination.annotation.CursorFieldMapping.*;

import java.lang.reflect.Field;
import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.akatsuki.newsum.common.dto.ErrorCodeAndMessage;
import com.akatsuki.newsum.common.exception.BusinessException;
import com.akatsuki.newsum.common.pagination.annotation.CursorFieldMapping;
import com.akatsuki.newsum.common.pagination.model.cursor.CreatedAtIdCursor;
import com.akatsuki.newsum.common.pagination.model.cursor.Cursor;

@Component
public class CreatedAtIdCursorGenerator implements CursorGenerator {

	@Override
	public boolean supports(Class<? extends Cursor> cursorType) {
		return cursorType.equals(CreatedAtIdCursor.class);
	}

	@Override
	public <T> Cursor generate(T element) {
		try {
			Class<?> clazz = element.getClass();

			// 어노테이션 유무 확인
			CursorFieldMapping mapping = clazz.getAnnotation(CursorFieldMapping.class);
			String createdAtFieldName = (mapping != null) ? mapping.createdAt() : DEFAULT_CREATED_AT_FIELD_NAME;
			String idFieldName = (mapping != null) ? mapping.id() : DEFAULT_ID_FIELD_NAME;

			Field createdAtField = clazz.getDeclaredField(createdAtFieldName);
			Field idField = clazz.getDeclaredField(idFieldName);

			createdAtField.setAccessible(true);
			idField.setAccessible(true);

			LocalDateTime createdAt = (LocalDateTime)createdAtField.get(element);
			Long id = (Long)idField.get(element);

			return new CreatedAtIdCursor(createdAt, id);
		} catch (NoSuchFieldException | IllegalAccessException | ClassCastException e) {
			throw new BusinessException(ErrorCodeAndMessage.CURSOR_WRONG_EXPRESSION);
		}
	}
}
