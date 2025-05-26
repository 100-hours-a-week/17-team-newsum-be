package com.akatsuki.newsum.domain.webtoon.repository;

import java.util.List;

import com.akatsuki.newsum.common.pagination.model.cursor.CreatedAtIdCursor;
import com.akatsuki.newsum.domain.webtoon.entity.webtoon.QWebtoon;
import com.akatsuki.newsum.domain.webtoon.entity.webtoon.QWebtoonFavorite;
import com.akatsuki.newsum.domain.webtoon.entity.webtoon.WebtoonFavorite;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class WebtoonFavoriteRepositoryImpl implements WebtoonFavoriteRepositoryCustom {
	private final JPAQueryFactory queryFactory;

	@Override
	public List<WebtoonFavorite> findFavoritesByUserIdWithCursor(Long userId, CreatedAtIdCursor cursor, int size) {
		QWebtoonFavorite qWebtoonFavorite = QWebtoonFavorite.webtoonFavorite;
		QWebtoon webtoon = QWebtoon.webtoon;

		BooleanBuilder builder = new BooleanBuilder()
			.and(qWebtoonFavorite.user.id.eq(userId));

		if (cursor.getCreatedAt() != null && cursor.getId() != null) {
			builder.and(
				qWebtoonFavorite.createdAt.lt(
					cursor.getCreatedAt()
				).or(qWebtoonFavorite.createdAt.eq(cursor.getCreatedAt()))
			).and(qWebtoonFavorite.id.lt(cursor.getId()));
		}

		return queryFactory
			.selectFrom(qWebtoonFavorite)
			.join(qWebtoonFavorite.webtoon, webtoon).fetchJoin()
			.where(builder)
			.orderBy(qWebtoonFavorite.createdAt.desc(), qWebtoonFavorite.id.desc())
			.limit(size + 1)
			.fetch();
	}

}
