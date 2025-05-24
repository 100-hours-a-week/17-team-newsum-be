package com.akatsuki.newsum.domain.webtoon.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.akatsuki.newsum.domain.webtoon.entity.webtoon.WebtoonFavorite;

public interface WebtoonFavoriteRepository extends JpaRepository<WebtoonFavorite, Long> {

	//상세페이지 들어갔을때 북마크했는지 아닌지 확인및 북마크 할 용도
	boolean existsByWebtoonIdAndUserId(Long webtoonId, Long userId);

	//유저가 북마크한 전체 웹툰들 북마크 페이지에서 출력
	List<WebtoonFavorite> findByUserId(Long userId);

	void deleteByUserIdAndWebtoonId(Long userId, Long webtoonId);

	Optional<WebtoonFavorite> findByWebtoonIdAndUserId(Long webtoonId, Long userId);

}
