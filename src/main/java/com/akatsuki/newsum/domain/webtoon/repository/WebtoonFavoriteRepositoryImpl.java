package com.akatsuki.newsum.domain.webtoon.repository;

import static com.akatsuki.newsum.domain.webtoon.entity.webtoon.QWebtoon.*;
import static com.akatsuki.newsum.domain.webtoon.entity.webtoon.QWebtoonFavorite.*;

import java.util.List;

import com.akatsuki.newsum.common.pagination.model.cursor.Cursor;
import com.akatsuki.newsum.common.pagination.model.query.QueryEngineType;
import com.akatsuki.newsum.common.pagination.model.query.QueryFragment;
import com.akatsuki.newsum.common.pagination.query.CursorPageQueryBuilder;
import com.akatsuki.newsum.common.pagination.query.registry.CursorPageQueryRegistry;
import com.akatsuki.newsum.domain.webtoon.entity.webtoon.QWebtoon;
import com.akatsuki.newsum.domain.webtoon.entity.webtoon.WebtoonFavorite;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class WebtoonFavoriteRepositoryImpl implements WebtoonFavoriteRepositoryCustom {
	private final JPAQueryFactory queryFactory;
	private final CursorPageQueryRegistry cursorQueryRegistry;

	@Override
	public List<WebtoonFavorite> findFavoritesByUserIdWithCursor(Long userId, Cursor cursor, int size) {
		QueryFragment queryFragment = buildQueryFragment(cursor);

		return queryFactory
			.selectFrom(webtoonFavorite)
			.join(webtoonFavorite.webtoon, webtoon).fetchJoin()
			.where(webtoonFavorite.user.id.eq(userId)
				.and((Predicate)queryFragment.whereClause()))
			.orderBy((OrderSpecifier<?>[])queryFragment.orderByClause())
			.limit(size + 1)
			.fetch();
	}

	private QueryFragment buildQueryFragment(Cursor cursor) {
		CursorPageQueryBuilder<Cursor> queryBuilder = cursorQueryRegistry.resolve(QWebtoon.class, cursor,
			QueryEngineType.QUERYDSL);
		QueryFragment queryFragment = queryBuilder.buildQueryFragment(cursor);
		return queryFragment;
	}
}
