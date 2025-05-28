package com.akatsuki.newsum.domain.webtoon.repository;

import static com.akatsuki.newsum.domain.webtoon.entity.webtoon.QWebtoon.*;
import static com.akatsuki.newsum.domain.webtoon.entity.webtoon.QWebtoonFavorite.*;

import java.util.List;

import com.akatsuki.newsum.common.pagination.model.cursor.CreatedAtIdCursor;
import com.akatsuki.newsum.domain.webtoon.entity.webtoon.WebtoonFavorite;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class WebtoonFavoriteRepositoryImpl implements WebtoonFavoriteRepositoryCustom {
	private final JPAQueryFactory queryFactory;

	@Override
	public List<WebtoonFavorite> findFavoritesByUserIdWithCursor(Long userId, CreatedAtIdCursor cursor, int size) {

		BooleanBuilder builder = new BooleanBuilder()
			.and(webtoonFavorite.user.id.eq(userId));

		if (cursor.getCreatedAt() != null && cursor.getId() != null) {
			builder.and(webtoonFavorite.createdAt.gt(cursor.getCreatedAt()))
				.or(
					webtoonFavorite.createdAt.eq(cursor.getCreatedAt())
						.and(webtoonFavorite.id.loe(cursor.getId()))
				);
		}

		return queryFactory
			.selectFrom(webtoonFavorite)
			.join(webtoonFavorite.webtoon, webtoon).fetchJoin()
			.where(builder)
			.orderBy(webtoonFavorite.createdAt.desc(), webtoonFavorite.id.desc())
			.limit(size + 1)
			.fetch();
	}
}
