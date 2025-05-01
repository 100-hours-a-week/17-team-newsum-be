package com.akatsuki.newsum.domain.webtoon.repository;

import static com.akatsuki.newsum.domain.aiAuthor.entity.QAiAuthor.*;
import static com.akatsuki.newsum.domain.webtoon.entity.webtoon.QNewsSource.*;
import static com.akatsuki.newsum.domain.webtoon.entity.webtoon.QWebtoon.*;

import java.util.Optional;

import com.akatsuki.newsum.domain.webtoon.entity.webtoon.Webtoon;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class WebtoonQueryRepositoryImpl implements WebtoonQueryRepository {

	private final JPAQueryFactory queryFactory;

	@Override
	public Optional<Webtoon> findWebtoonAndAiAuthorById(Long webtoonId) {
		return queryFactory
			.selectFrom(webtoon)
			.join(webtoon.aiAuthor, aiAuthor).fetchJoin()
			.where(webtoon.id.eq(webtoonId))
			.fetch()
			.stream().findFirst();
	}

	@Override
	public Optional<Webtoon> findWebtoonAndNewsSourceById(Long webtoonId) {
		return queryFactory
			.selectFrom(webtoon)
			.leftJoin(webtoon.newsSources, newsSource).fetchJoin()
			.where(webtoon.id.eq(webtoonId))
			.fetch()
			.stream().findFirst();
	}
}
