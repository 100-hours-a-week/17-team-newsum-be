package com.akatsuki.newsum.domain.webtoon.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.akatsuki.newsum.domain.webtoon.entity.webtoon.WebtoonLike;

public interface WebtoonLikeRepository extends JpaRepository<WebtoonLike, Long> {

	Optional<WebtoonLike> findByWebtoonIdAndUserId(Long webtoonId, Long userId);

	boolean existsByWebtoonIdAndUserId(Long webtoonId, Long userId);

	long countByWebtoonId(Long webtoonId);

}

