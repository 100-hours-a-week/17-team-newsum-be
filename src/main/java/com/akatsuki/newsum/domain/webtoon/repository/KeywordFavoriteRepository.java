package com.akatsuki.newsum.domain.webtoon.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.akatsuki.newsum.domain.webtoon.entity.webtoon.KeywordFavorite;

public interface KeywordFavoriteRepository extends JpaRepository<KeywordFavorite, Long> {
	boolean existsByUserIdAndKeywordId(Long userId, Long keywordId);
}
