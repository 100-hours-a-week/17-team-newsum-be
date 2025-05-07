package com.akatsuki.newsum.domain.webtoon.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.akatsuki.newsum.domain.webtoon.entity.webtoon.RecentView;

public interface RecentViewRepository extends JpaRepository<RecentView, Long>, RecentViewQueryRepository {
}

