package com.akatsuki.newsum.domain.webtoon.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.akatsuki.newsum.domain.webtoon.entity.webtoon.NewsSource;

public interface NewsSourceRepository extends JpaRepository<NewsSource, Long> {
}
