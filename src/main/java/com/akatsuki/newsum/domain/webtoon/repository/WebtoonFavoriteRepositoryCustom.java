package com.akatsuki.newsum.domain.webtoon.repository;

import java.util.List;

import com.akatsuki.newsum.common.pagination.model.cursor.Cursor;
import com.akatsuki.newsum.domain.webtoon.entity.webtoon.WebtoonFavorite;

public interface WebtoonFavoriteRepositoryCustom {
	List<WebtoonFavorite> findFavoritesByUserIdWithCursor(Long userId, Cursor cursor, int size);

}
