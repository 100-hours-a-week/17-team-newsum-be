package com.akatsuki.newsum.domain.webtoon.repository;

import java.util.Optional;

import com.akatsuki.newsum.domain.webtoon.entity.webtoon.Webtoon;

public interface WebtoonQueryRepository {

	Optional<Webtoon> findWebtoonAndAiAuthorById(Long webtoonId);

	Optional<Webtoon> findWebtoonAndNewsSourceById(Long webtoonId);
}
