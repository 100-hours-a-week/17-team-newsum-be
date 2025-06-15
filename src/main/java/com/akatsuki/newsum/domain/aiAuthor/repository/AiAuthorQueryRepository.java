package com.akatsuki.newsum.domain.aiAuthor.repository;

import java.util.List;

import com.akatsuki.newsum.common.pagination.model.cursor.Cursor;
import com.akatsuki.newsum.domain.aiAuthor.entity.AiAuthor;

public interface AiAuthorQueryRepository {
	List<AiAuthor> findByCursorAndSize(Cursor cursor, int size);
}
