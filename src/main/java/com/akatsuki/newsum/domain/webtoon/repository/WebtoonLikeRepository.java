package com.akatsuki.newsum.domain.webtoon.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.akatsuki.newsum.domain.webtoon.entity.webtoon.WebtoonLike;

public interface WebtoonLikeRepository extends JpaRepository<WebtoonLike, Long> {

	Optional<WebtoonLike> findByWebtoonAndUser(Long webtoon, Long user);

	// 들어갔을때 좋아요했는지 아닌지 확인, 좋아요할 용도
	boolean existsByWebtoonIdAndUserId(Long webtoonId, Long userId);

	long countByWebtoon(Long webtoonId);

}

