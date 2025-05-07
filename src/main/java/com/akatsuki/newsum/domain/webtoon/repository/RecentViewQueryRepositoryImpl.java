package com.akatsuki.newsum.domain.webtoon.repository;

import java.util.List;

import com.akatsuki.newsum.domain.webtoon.entity.webtoon.QRecentView;
import com.akatsuki.newsum.domain.webtoon.entity.webtoon.Webtoon;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RecentViewQueryRepositoryImpl implements RecentViewQueryRepository {

	private final JPAQueryFactory queryFactory;
	private final QRecentView recentView = QRecentView.recentView;

	@Override
	public List<Webtoon> findRecentWebtoonsByUserId(Long userId, int limit) {
		return queryFactory
			.select(recentView.webtoon)
			.from(recentView)
			.where(recentView.user.id.eq(userId))
			.orderBy(recentView.viewedAt.desc())
			.limit(limit)
			.fetch();
	}
}


