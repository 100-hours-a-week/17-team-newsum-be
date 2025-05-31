package com.akatsuki.newsum.domain.webtoon.repository;

import static com.akatsuki.newsum.domain.webtoon.entity.webtoon.QRecentView.*;

import java.util.List;

import com.akatsuki.newsum.domain.webtoon.entity.webtoon.Webtoon;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RecentViewQueryRepositoryImpl implements RecentViewQueryRepository {

	private final JPAQueryFactory queryFactory;

	@Override
	public List<Webtoon> findRecentWebtoonsByUserId(Long userId, int RECENT_WEBTOON_LIMIT) {
		return queryFactory
			.select(recentView.webtoon)
			.from(recentView)
			.where(recentView.user.id.eq(userId))
			.orderBy(recentView.viewedAt.desc())
			.limit(RECENT_WEBTOON_LIMIT)
			.fetch();
	}
}


