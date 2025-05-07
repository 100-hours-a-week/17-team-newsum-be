package com.akatsuki.newsum.domain.webtoon.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.akatsuki.newsum.common.dto.ErrorCodeAndMessage;
import com.akatsuki.newsum.common.exception.NotFoundException;
import com.akatsuki.newsum.common.pagination.model.cursor.Cursor;
import com.akatsuki.newsum.domain.aiAuthor.entity.AiAuthor;
import com.akatsuki.newsum.domain.aiAuthor.repository.AiAuthorRepository;
import com.akatsuki.newsum.domain.webtoon.dto.AiAuthorInfoDto;
import com.akatsuki.newsum.domain.webtoon.dto.CreateWebtoonReqeust;
import com.akatsuki.newsum.domain.webtoon.dto.WebtoonCardDto;
import com.akatsuki.newsum.domain.webtoon.dto.WebtoonDetailResponse;
import com.akatsuki.newsum.domain.webtoon.dto.WebtoonResponse;
import com.akatsuki.newsum.domain.webtoon.dto.WebtoonSlideDto;
import com.akatsuki.newsum.domain.webtoon.dto.WebtoonSourceDto;
import com.akatsuki.newsum.domain.webtoon.entity.webtoon.Category;
import com.akatsuki.newsum.domain.webtoon.entity.webtoon.NewsSource;
import com.akatsuki.newsum.domain.webtoon.entity.webtoon.Webtoon;
import com.akatsuki.newsum.domain.webtoon.entity.webtoon.WebtoonDetail;
import com.akatsuki.newsum.domain.webtoon.exception.WebtoonNotFoundException;
import com.akatsuki.newsum.domain.webtoon.repository.NewsSourceRepository;
import com.akatsuki.newsum.domain.webtoon.repository.WebtoonDetailRepository;
import com.akatsuki.newsum.domain.webtoon.repository.WebtoonRepository;
import com.akatsuki.newsum.extern.dto.CreateWebtoonApiRequest;
import com.akatsuki.newsum.extern.service.AiServerApiService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WebtoonService {

	private final WebtoonRepository webtoonRepository;
	private final AiAuthorRepository aiAuthorRepository;
	private final WebtoonDetailRepository webtoonDetailRepository;
	private final NewsSourceRepository newsSourceRepository;
	private final AiServerApiService aiServerApiService;
	private final RecentViewRepository recentViewRepository;

	private final int RELATED_CATEGORY_SIZE = 2;
	private final int RELATED_AI_AUTHOR_SIZE = 2;
	private final int RELATED_NEWS_SIZE = RELATED_CATEGORY_SIZE + RELATED_AI_AUTHOR_SIZE;

	public List<WebtoonCardDto> findWebtoonsByCategory(String category, Cursor cursor, int size) {
		List<Webtoon> webtoons = webtoonRepository.findWebtoonByCategoryWithCursor(Category.valueOf(category), cursor,
			size);

		return webtoons.stream()
			.map(this::mapWebToonCardDto)
			.toList();
	}

	public WebtoonResponse getWebtoon(Long webtoonId, Long userId) {
		Webtoon webtoon = findWebtoonWithAiAuthorByIdOrThrow(webtoonId);

		// 좋아요, 북마크 여부 확인
		boolean isLiked = false;
		boolean isBookmarked = false;

		//TODO : 좋아요 테이블, 즐겨찾기 테이블 연결 필요
		if (userId == null) {

		} else {

		}
		return new WebtoonResponse(
			webtoon.getId(),
			mapWebToonSlides(webtoon),
			mapAiAuthorToAiAuthorInfoDto(webtoon.getAiAuthor()),
			isLiked,
			isBookmarked,
			webtoon.getLikeCount(),
			webtoon.getViewCount()
		);
	}

	public WebtoonDetailResponse getWebtoonDetail(Long webtoonId) {
		Webtoon webtoon = findWebtoonWithAiAuthorByIdOrThrow(webtoonId);

		List<WebtoonCardDto> relatedNews = fetchRelatedNews(webtoon);

		List<WebtoonSourceDto> sources = getNewsSources(webtoon);

		LocalDateTime createdAt = webtoon.getCreatedAt();

		Long commentCount = webtoon.getParentCommentCount();

		return new WebtoonDetailResponse(sources,
			relatedNews,
			createdAt,
			commentCount);
	}

	@Transactional
	public void createWebtoon(CreateWebtoonReqeust request) {
		AiAuthor aiAuthor = findAiAuthorById(request.aiAuthorId());

		Webtoon webtoon = new Webtoon(aiAuthor,
			Category.valueOf(request.category()),
			request.title(),
			request.content(),
			request.thumbnailImageUrl());

		Webtoon save = webtoonRepository.save(webtoon);

		List<NewsSource> newsSources = request.sourceNews().stream()
			.map(newsSourceDto -> new NewsSource(save, newsSourceDto.headline(), newsSourceDto.url()))
			.toList();

		List<WebtoonDetail> webtoonDetails = request.slides().stream()
			.map(webtoonSlideDto -> new WebtoonDetail(save, webtoonSlideDto.imageUrl(), webtoonSlideDto.content(),
				webtoonSlideDto.slideSeq()))
			.toList();

		webtoonDetailRepository.saveAll(webtoonDetails);
		newsSourceRepository.saveAll(newsSources);
	}

	public void createWebtoonTest(Long authorId) {
		aiServerApiService.createWebtoonApi(new CreateWebtoonApiRequest(authorId, null));
	}

	private AiAuthor findAiAuthorById(Long id) {
		return aiAuthorRepository.findById(id)
			.orElseThrow(() -> new NotFoundException(ErrorCodeAndMessage.AI_AUTHOR_NOT_FOUND));
	}

	private List<WebtoonCardDto> fetchRelatedNews(Webtoon webtoon) {
		List<Webtoon> webtoonByCategory = webtoonRepository.findWebtoonByCategory(webtoon.getCategory());
		List<Webtoon> webtoonByAiAuthor = webtoonRepository.findWebtoonByAiAuthor(webtoon.getAiAuthor());

		webtoonByCategory.remove(webtoon);
		webtoonByAiAuthor.remove(webtoon);

		webtoonByAiAuthor = removeDuplicateWebtoons(webtoonByAiAuthor, webtoonByCategory);

		int aiAuthorCount = Math.min(webtoonByAiAuthor.size(), RELATED_AI_AUTHOR_SIZE);
		int categoryCount = Math.min(webtoonByCategory.size(), RELATED_NEWS_SIZE - aiAuthorCount);

		Collections.shuffle(webtoonByCategory);
		Collections.shuffle(webtoonByAiAuthor);

		List<WebtoonCardDto> result = new ArrayList<>();
		result.addAll(webtoonByCategory.stream()
			.limit(categoryCount)
			.map(this::mapWebToonCardDto)
			.toList());

		result.addAll(webtoonByAiAuthor.stream()
			.limit(aiAuthorCount)
			.map(this::mapWebToonCardDto)
			.toList());

		return result;
	}

	private List<Webtoon> removeDuplicateWebtoons(List<Webtoon> targetWebtoons, List<Webtoon> toRemoveWebtoons) {
		Set<Webtoon> toRemoveWetoonSet = new HashSet<>(toRemoveWebtoons);

		return targetWebtoons.stream()
			.filter(w -> !toRemoveWetoonSet.contains(w))
			.collect(Collectors.toList());
	}

	private WebtoonCardDto mapWebToonCardDto(Webtoon webtoon) {
		return new WebtoonCardDto(webtoon.getId(), webtoon.getTitle(), webtoon.getThumbnailImageUrl(),
			webtoon.getCreatedAt());
	}

	private AiAuthorInfoDto mapAiAuthorToAiAuthorInfoDto(AiAuthor aiAuthor) {
		return new AiAuthorInfoDto(aiAuthor.getId(), aiAuthor.getName(), aiAuthor.getProfileImageUrl());
	}

	private List<WebtoonSlideDto> mapWebToonSlides(Webtoon webtoon) {
		return webtoon.getDetails().stream()
			.map(this::mapSingleDetialToWebtoonSlide)
			.sorted(Comparator.comparing(WebtoonSlideDto::slideSeq))
			.toList();
	}

	private WebtoonSlideDto mapSingleDetialToWebtoonSlide(WebtoonDetail webtoonDetail) {
		return new WebtoonSlideDto(webtoonDetail.getImageSeq(), webtoonDetail.getImageUrl(),
			webtoonDetail.getContent());
	}

	private List<WebtoonSourceDto> getNewsSources(Webtoon webtoon) {
		return webtoon.getNewsSources().stream()
			.map(this::mapSingWebtoonSourceDto)
			.toList();
	}

	private WebtoonSourceDto mapSingWebtoonSourceDto(NewsSource newsSource) {
		return new WebtoonSourceDto(newsSource.getHeadline(), newsSource.getUrl());
	}

	private Webtoon findWebtoonWithAiAuthorByIdOrThrow(Long webtoonId) {
		return webtoonRepository.findWebtoonAndAiAuthorById(webtoonId)
			.orElseThrow(WebtoonNotFoundException::new);
	}

	private Webtoon findWebtoonWithNewSourceById(Long webtoonId) {
		return webtoonRepository.findWebtoonAndNewsSourceById(webtoonId)
			.orElseThrow(WebtoonNotFoundException::new);
	}

	public List<WebtoonCardDto> getTop3TodayByViewCount() {
		return webtoonRepository.findTop3TodayByViewCount().stream()
			.map(WebtoonCardDto::toDto)
			.toList();
	}

	public List<WebtoonCardDto> getTodayNewsCards() {
		return webtoonRepository.findTodayNewsTop3().stream()
			.map(WebtoonCardDto::toDto)
			.toList();
	}

	public Map<String, List<WebtoonCardDto>> getWebtoonsByCategoryLimit3() {
		Map<String, List<WebtoonCardDto>> result = new LinkedHashMap<>();
		for (Category category : Category.values()) {
			List<WebtoonCardDto> dtoList = webtoonRepository.findTop3ByCategoryOrderByCreatedAtDesc(category)
				.stream()
				.map(WebtoonCardDto::toDto)
				.toList();
			result.put(category.name(), dtoList);
		}
		return result;
	}

	public List<WebtoonCardDto> getRecentWebtoons(Long userId) {
		return recentViewQueryRepository.findRecentWebtoonsByUserId(userId, 3).stream()
			.map(WebtoonCardDto::toDto)
			.toList();
	}
}
