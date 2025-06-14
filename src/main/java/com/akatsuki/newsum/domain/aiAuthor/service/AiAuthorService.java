package com.akatsuki.newsum.domain.aiAuthor.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.akatsuki.newsum.common.dto.ErrorCodeAndMessage;
import com.akatsuki.newsum.common.exception.BusinessException;
import com.akatsuki.newsum.domain.aiAuthor.entity.AiAuthor;
import com.akatsuki.newsum.domain.aiAuthor.repository.AiAuthorRepository;
import com.akatsuki.newsum.domain.user.entity.AuthorFavorite;
import com.akatsuki.newsum.domain.user.repository.AiAuthorFavoriteRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional
@Service
public class AiAuthorService {

	private final AiAuthorFavoriteRepository aiAuthorFavoriteRepository;
	private final AiAuthorRepository aiAuthorRepository;

	public void toggleSubscribe(Long userId, Long aiAuthorId) {
		AiAuthor author = findAuthorById(aiAuthorId);
		findFavorite(userId, aiAuthorId).ifPresentOrElse(
			aiAuthorFavoriteRepository::delete,
			() -> aiAuthorFavoriteRepository.save(new AuthorFavorite(userId, author))
		);
	}

	private AiAuthor findAuthorById(Long aiAuthorId) {
		return aiAuthorRepository.findById(aiAuthorId)
			.orElseThrow(() -> new BusinessException(ErrorCodeAndMessage.AI_AUTHOR_NOT_FOUND));
	}

	private Optional<AuthorFavorite> findFavorite(Long userId, Long aiAuthorId) {
		return aiAuthorFavoriteRepository.findByUserIdAndAiAuthorId(userId, aiAuthorId);
	}
}
