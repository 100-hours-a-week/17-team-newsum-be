package com.akatsuki.newsum.domain.aiAuthor.service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.akatsuki.newsum.common.dto.ErrorCodeAndMessage;
import com.akatsuki.newsum.common.exception.BusinessException;
import com.akatsuki.newsum.common.pagination.CursorPaginationService;
import com.akatsuki.newsum.common.pagination.model.cursor.Cursor;
import com.akatsuki.newsum.common.pagination.model.page.CursorPage;
import com.akatsuki.newsum.domain.aiAuthor.dto.AiAuthorDetailResponse;
import com.akatsuki.newsum.domain.aiAuthor.dto.AiAuthorListItemResponse;
import com.akatsuki.newsum.domain.aiAuthor.dto.AiAuthorListResponse;
import com.akatsuki.newsum.domain.aiAuthor.dto.AiAuthorWebtoonResponse;
import com.akatsuki.newsum.domain.aiAuthor.entity.AiAuthor;
import com.akatsuki.newsum.domain.aiAuthor.repository.AiAuthorRepository;
import com.akatsuki.newsum.domain.user.entity.AuthorFavorite;
import com.akatsuki.newsum.domain.user.repository.AiAuthorFavoriteRepository;
import com.akatsuki.newsum.domain.webtoon.entity.webtoon.Webtoon;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional
@Service
public class AiAuthorService {

	private final AiAuthorFavoriteRepository aiAuthorFavoriteRepository;
	private final AiAuthorRepository aiAuthorRepository;
	private final CursorPaginationService cursorPaginationService;

	public void toggleSubscribe(Long userId, Long aiAuthorId) {
		AiAuthor author = findAuthorById(aiAuthorId);
		findFavorite(userId, aiAuthorId).ifPresentOrElse(
			aiAuthorFavoriteRepository::delete,
			() -> aiAuthorFavoriteRepository.save(new AuthorFavorite(userId, author))
		);
	}

	@Transactional(readOnly = true)
	public AiAuthorDetailResponse getAuthorDetail(Long aiAuthorId) {
		AiAuthor author = findAuthorById(aiAuthorId);
		List<AiAuthorWebtoonResponse> webtoons = getAuthorWebtoonsSortedByLatest(author);
		return toDetailResponse(author, webtoons);
	}

	public AiAuthorListResponse getAuthorList(Cursor cursor, int size) {
		CursorPage<AiAuthor> page = findAiAuthors(cursor, size);

		List<AiAuthorListItemResponse> items = page.getItems().stream()
			.map(this::mapToListItemResponse)
			.toList();

		return new AiAuthorListResponse(items, page.getPageInfo());
	}

	private AiAuthor findAuthorById(Long aiAuthorId) {
		return aiAuthorRepository.findById(aiAuthorId)
			.orElseThrow(() -> new BusinessException(ErrorCodeAndMessage.AI_AUTHOR_NOT_FOUND));
	}

	private Optional<AuthorFavorite> findFavorite(Long userId, Long aiAuthorId) {
		return aiAuthorFavoriteRepository.findByUserIdAndAiAuthorId(userId, aiAuthorId);
	}

	private AiAuthorDetailResponse toDetailResponse(AiAuthor author, List<AiAuthorWebtoonResponse> webtoons) {
		return new AiAuthorDetailResponse(
			author.getId(),
			author.getName(),
			author.getStyle(),
			author.getIntroduction(),
			author.getProfileImageUrl(),
			webtoons
		);
	}

	private AiAuthorWebtoonResponse mapToWebtoonResponse(Webtoon webtoon) {
		return new AiAuthorWebtoonResponse(
			webtoon.getId(),
			webtoon.getTitle(),
			webtoon.getThumbnailImageUrl()
		);
	}

	private List<AiAuthorWebtoonResponse> getAuthorWebtoonsSortedByLatest(AiAuthor author) {
		return author.getWebtoons().stream()
			.sorted(Comparator.comparing(Webtoon::getCreatedAt).reversed())
			.map(this::mapToWebtoonResponse)
			.toList();
	}

	private AiAuthorListItemResponse mapToListItemResponse(AiAuthor author) {
		return new AiAuthorListItemResponse(
			author.getId(),
			author.getName(),
			author.getProfileImageUrl(),
			author.getCreatedAt()
		);
	}

	private CursorPage<AiAuthor> findAiAuthors(Cursor cursor, int size) {
		List<AiAuthor> authors = aiAuthorRepository.findByCursorAndSize(cursor, size);
		return cursorPaginationService.create(authors, size, cursor);
	}
}
