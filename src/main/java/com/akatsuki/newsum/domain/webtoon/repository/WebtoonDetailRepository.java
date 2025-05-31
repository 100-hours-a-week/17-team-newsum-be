package com.akatsuki.newsum.domain.webtoon.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.akatsuki.newsum.domain.webtoon.entity.webtoon.Webtoon;
import com.akatsuki.newsum.domain.webtoon.entity.webtoon.WebtoonDetail;

public interface WebtoonDetailRepository extends JpaRepository<WebtoonDetail, Long> {
	List<WebtoonDetail> findWebtoonDetailsByWebtoon(Webtoon webtoon);
}
