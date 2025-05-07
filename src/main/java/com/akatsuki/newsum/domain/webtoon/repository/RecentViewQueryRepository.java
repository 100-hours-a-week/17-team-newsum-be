package com.akatsuki.newsum.domain.webtoon.repository;

import java.util.List;

import com.akatsuki.newsum.domain.webtoon.entity.webtoon.Webtoon;

public interface RecentViewQueryRepository {
	List<Webtoon> findRecentWebtoonsByUserId(Long userId, int limit);
}
