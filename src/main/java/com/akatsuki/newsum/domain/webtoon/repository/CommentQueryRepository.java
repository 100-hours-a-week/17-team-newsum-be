package com.akatsuki.newsum.domain.webtoon.repository;

import java.util.List;

import com.akatsuki.newsum.common.pagination.model.cursor.Cursor;
import com.akatsuki.newsum.domain.webtoon.dto.CommentReadDto;

public interface CommentQueryRepository {

	List<CommentReadDto> findParentCommentsByCursorAndSize(Long webtoonId, Cursor cursor, int size);

	List<CommentReadDto> findByWebtoonIdAndParentCommentIdIn(Long webtoonId, List<Long> parentCommentIds);
}
