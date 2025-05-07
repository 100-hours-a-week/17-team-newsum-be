package com.akatsuki.newsum.domain.aiAuthor.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.akatsuki.newsum.domain.aiAuthor.entity.AiAuthor;

public interface AiAuthorRepository extends JpaRepository<AiAuthor, Long> {
}
