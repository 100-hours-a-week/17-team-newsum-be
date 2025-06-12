package com.akatsuki.newsum.extern.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.akatsuki.newsum.domain.webtoon.entity.webtoon.ImageGenerationQueue;

public interface ImageGenerationQueueRepository extends JpaRepository<ImageGenerationQueue, Long> {
}
