package com.akatsuki.newsum.domain.webtoon.service;

import static com.akatsuki.newsum.fixture.entity.AiAuthorFixture.*;
import static com.akatsuki.newsum.fixture.entity.WebtoonDomainFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.akatsuki.newsum.common.pagination.model.cursor.CreatedAtIdCursor;
import com.akatsuki.newsum.common.pagination.model.cursor.Cursor;
import com.akatsuki.newsum.domain.aiAuthor.entity.AiAuthor;
import com.akatsuki.newsum.domain.user.entity.User;
import com.akatsuki.newsum.domain.user.repository.UserRepository;
import com.akatsuki.newsum.domain.webtoon.dto.WebtoonCardDto;
import com.akatsuki.newsum.domain.webtoon.entity.webtoon.Category;
import com.akatsuki.newsum.domain.webtoon.entity.webtoon.RecentView;
import com.akatsuki.newsum.domain.webtoon.entity.webtoon.Webtoon;
import com.akatsuki.newsum.domain.webtoon.entity.webtoon.WebtoonFavorite;
import com.akatsuki.newsum.domain.webtoon.exception.WebtoonNotFoundException;
import com.akatsuki.newsum.domain.webtoon.repository.RecentViewRepository;
import com.akatsuki.newsum.domain.webtoon.repository.WebtoonFavoriteRepository;
import com.akatsuki.newsum.domain.webtoon.repository.WebtoonRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("WebtoonService 테스트")
class WebtoonServiceTest {

	@Mock
	private WebtoonFavoriteRepository webtoonFavoriteRepository;

	@Mock
	private WebtoonRepository webtoonRepository;

	@Mock
	private RecentViewRepository recentViewRepository;

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private WebtoonService webtoonService;

	private AiAuthor mockAiAuthor;
	private Webtoon mockWebtoon;
	private User mockUser;

	@BeforeEach
	void setUp() {
		mockAiAuthor = createAiAuthor();
		mockWebtoon = createWebtoonWithTitle("테스트 웹툰");
		mockUser = User.builder()
			.email("test@example.com")
			.nickname("테스트 유저")
			.build();
	}

	@Nested
	@DisplayName("카테고리별 웹툰 조회 테스트")
	class FindWebtoonsByCategoryTest {

		@Test
		@DisplayName("카테고리별 웹툰 목록을 정상적으로 조회하고 올바른 DTO로 변환한다")
		void findWebtoonsByCategory_Success() {
			// given
			String category = "IT";
			Cursor cursor = new CreatedAtIdCursor(LocalDateTime.now(), 100L);
			int size = 10;

			List<Webtoon> mockWebtoons = createWebtoonList(2);

			given(webtoonRepository.findWebtoonByCategoryWithCursor(Category.IT, cursor, size))
				.willReturn(mockWebtoons);

			// when
			List<WebtoonCardDto> results = webtoonService.findWebtoonsByCategory(category, cursor, size);

			// then
			assertThat(results).hasSize(2);

			// DTO 변환 검증
			WebtoonCardDto firstResult = results.get(0);
			Webtoon firstWebtoon = mockWebtoons.get(0);
			assertThat(firstResult.id()).isEqualTo(firstWebtoon.getId());
			assertThat(firstResult.title()).isEqualTo(firstWebtoon.getTitle());
			assertThat(firstResult.thumbnailUrl()).isEqualTo(firstWebtoon.getThumbnailImageUrl());
			assertThat(firstResult.viewCount()).isEqualTo(firstWebtoon.getViewCount());

			verify(webtoonRepository).findWebtoonByCategoryWithCursor(Category.IT, cursor, size);
		}

		@Test
		@DisplayName("존재하지 않는 카테고리로 조회 시 예외가 발생한다")
		void findWebtoonsByCategory_InvalidCategory() {
			// given
			String invalidCategory = "INVALID_CATEGORY";
			Cursor cursor = new CreatedAtIdCursor(LocalDateTime.now(), 100L);
			int size = 10;

			// when & then
			assertThatThrownBy(() -> webtoonService.findWebtoonsByCategory(invalidCategory, cursor, size))
				.isInstanceOf(IllegalArgumentException.class);
		}
	}

	@Nested
	@DisplayName("조회수 업데이트 테스트")
	class UpdateViewCountTest {

		@Test
		@DisplayName("웹툰 조회수를 1 증가시킨다")
		void updateViewCount_Success() {
			// given
			Long webtoonId = 1L;
			Long initialViewCount = mockWebtoon.getViewCount();

			given(webtoonRepository.findById(webtoonId))
				.willReturn(Optional.of(mockWebtoon));

			// when
			webtoonService.updateViewCount(webtoonId);

			// then
			assertThat(mockWebtoon.getViewCount()).isEqualTo(initialViewCount + 1);
			verify(webtoonRepository).findById(webtoonId);
		}

		@Test
		@DisplayName("존재하지 않는 웹툰 조회 시 예외가 발생한다")
		void updateViewCount_WebtoonNotFound() {
			// given
			Long webtoonId = 999L;

			given(webtoonRepository.findById(webtoonId))
				.willReturn(Optional.empty());

			// when & then
			assertThatThrownBy(() -> webtoonService.updateViewCount(webtoonId))
				.isInstanceOf(WebtoonNotFoundException.class);
		}
	}

	@Nested
	@DisplayName("웹툰 검색 테스트")
	class SearchWebtoonsTest {

		@Test
		@DisplayName("제목으로 웹툰을 검색하고 올바른 DTO로 변환한다")
		void searchWebtoons_Success() {
			// given
			String query = "로맨스";
			Cursor cursor = new CreatedAtIdCursor(LocalDateTime.of(2024, 1, 1, 0, 0), 100L);
			int size = 3;

			List<Webtoon> mockWebtoons = List.of(
				createWebtoonWithTitle("로맨스 소설"),
				createWebtoonWithTitle("로맨스 액션")
			);

			given(webtoonRepository.searchByTitleContaining(query, cursor, size))
				.willReturn(mockWebtoons);

			// when
			List<WebtoonCardDto> results = webtoonService.searchWebtoons(query, cursor, size);

			// then
			assertThat(results).hasSize(2);

			// 첫 번째 결과의 DTO 변환 검증
			WebtoonCardDto firstResult = results.get(0);
			Webtoon firstWebtoon = mockWebtoons.get(0);
			assertThat(firstResult.title()).isEqualTo(firstWebtoon.getTitle());
			assertThat(firstResult.id()).isEqualTo(firstWebtoon.getId());
			assertThat(firstResult.thumbnailUrl()).isEqualTo(firstWebtoon.getThumbnailImageUrl());
			assertThat(firstResult.createdAt()).isEqualTo(firstWebtoon.getCreatedAt());
			assertThat(firstResult.viewCount()).isEqualTo(firstWebtoon.getViewCount());

			verify(webtoonRepository).searchByTitleContaining(query, cursor, size);
		}

		@Test
		@DisplayName("검색 결과가 없는 경우 빈 리스트를 반환한다")
		void searchWebtoons_EmptyResult() {
			// given
			String query = "존재하지않는검색어";
			Cursor cursor = new CreatedAtIdCursor(LocalDateTime.now(), 100L);
			int size = 10;

			given(webtoonRepository.searchByTitleContaining(query, cursor, size))
				.willReturn(List.of());

			// when
			List<WebtoonCardDto> results = webtoonService.searchWebtoons(query, cursor, size);

			// then
			assertThat(results).isEmpty();
			verify(webtoonRepository).searchByTitleContaining(query, cursor, size);
		}

		@Test
		@DisplayName("빈 검색어로 검색 시 예외가 발생한다")
		void searchWebtoons_EmptyQuery() {
			// given
			String emptyQuery = "";
			Cursor cursor = new CreatedAtIdCursor(LocalDateTime.now(), 100L);
			int size = 10;

			// when & then
			assertThatThrownBy(() -> webtoonService.searchWebtoons(emptyQuery, cursor, size))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessageContaining("검색어는 비어있을 수 없습니다");
		}
	}

	@Nested
	@DisplayName("최근 조회 업데이트 테스트")
	class UpdateRecentViewTest {

		@Test
		@DisplayName("로그인 사용자의 기존 최근 조회 기록을 업데이트한다")
		void updateRecentView_ExistingRecord() {
			// given
			Long webtoonId = 1L;
			Long userId = 1L;

			RecentView existingRecentView = mock(RecentView.class);
			given(recentViewRepository.findByWebtoonIdAndUserId(webtoonId, userId))
				.willReturn(Optional.of(existingRecentView));

			// when
			webtoonService.updateRecentView(webtoonId, userId);

			// then
			verify(recentViewRepository).findByWebtoonIdAndUserId(webtoonId, userId);
			verify(existingRecentView).updateViewedAt();
			verify(recentViewRepository, never()).save(any());
			verify(webtoonRepository, never()).findWebtoonAndAiAuthorById(any());
			verify(userRepository, never()).findById(any());
		}

		@Test
		@DisplayName("로그인 사용자의 최근 조회 기록이 없으면 새로 생성한다")
		void updateRecentView_NewRecord() {
			// given
			Long webtoonId = 1L;
			Long userId = 1L;

			given(recentViewRepository.findByWebtoonIdAndUserId(webtoonId, userId))
				.willReturn(Optional.empty());
			given(webtoonRepository.findWebtoonAndAiAuthorById(webtoonId))
				.willReturn(Optional.of(mockWebtoon));
			given(userRepository.findById(userId))
				.willReturn(Optional.of(mockUser));

			// when
			webtoonService.updateRecentView(webtoonId, userId);

			// then
			verify(recentViewRepository).findByWebtoonIdAndUserId(webtoonId, userId);
			verify(webtoonRepository).findWebtoonAndAiAuthorById(webtoonId);
			verify(userRepository).findById(userId);
			verify(recentViewRepository).save(any(RecentView.class));
		}

		@Test
		@DisplayName("비로그인 사용자는 최근 조회 기록을 업데이트하지 않는다")
		void updateRecentView_NonLoggedInUser() {
			// given
			Long webtoonId = 1L;
			Long userId = null;

			// when
			webtoonService.updateRecentView(webtoonId, userId);

			// then
			verify(recentViewRepository, never()).findByWebtoonIdAndUserId(any(), any());
			verify(recentViewRepository, never()).save(any());
		}

		@Test
		@DisplayName("존재하지 않는 웹툰에 대한 최근 조회 기록 생성 시 예외가 발생한다")
		void updateRecentView_WebtoonNotFound() {
			// given
			Long webtoonId = 999L;
			Long userId = 1L;

			given(recentViewRepository.findByWebtoonIdAndUserId(webtoonId, userId))
				.willReturn(Optional.empty());
			given(webtoonRepository.findWebtoonAndAiAuthorById(webtoonId))
				.willReturn(Optional.empty());

			// when & then
			assertThatThrownBy(() -> webtoonService.updateRecentView(webtoonId, userId))
				.isInstanceOf(WebtoonNotFoundException.class);
		}
	}

	@Nested
	@DisplayName("오늘의 인기 웹툰 조회 테스트")
	class GetTodayTop3Test {

		@Test
		@DisplayName("오늘의 조회수 TOP3 웹툰을 조회하고 올바른 순서로 정렬한다")
		void getTop3TodayByViewCount_Success() {
			// given
			List<Webtoon> mockWebtoons = List.of(
				createWebtoonWithViewCount("인기 웹툰 1", 1000L),
				createWebtoonWithViewCount("인기 웹툰 2", 800L),
				createWebtoonWithViewCount("인기 웹툰 3", 600L)
			);

			given(webtoonRepository.findTop3TodayByViewCount()).willReturn(mockWebtoons);

			// when
			List<WebtoonCardDto> results = webtoonService.getTop3TodayByViewCount();

			// then
			assertThat(results).hasSize(3);
			assertThat(results.get(0).viewCount()).isEqualTo(1000L);
			assertThat(results.get(1).viewCount()).isEqualTo(800L);
			assertThat(results.get(2).viewCount()).isEqualTo(600L);

			// 제목도 정확히 변환되는지 확인
			assertThat(results.get(0).title()).isEqualTo("인기 웹툰 1");
			assertThat(results.get(1).title()).isEqualTo("인기 웹툰 2");
			assertThat(results.get(2).title()).isEqualTo("인기 웹툰 3");

			verify(webtoonRepository).findTop3TodayByViewCount();
		}

		@Test
		@DisplayName("인기 웹툰이 3개 미만인 경우 해당 개수만큼 반환한다")
		void getTop3TodayByViewCount_LessThanThree() {
			// given
			List<Webtoon> mockWebtoons = List.of(
				createWebtoonWithViewCount("인기 웹툰 1", 1000L)
			);

			given(webtoonRepository.findTop3TodayByViewCount()).willReturn(mockWebtoons);

			// when
			List<WebtoonCardDto> results = webtoonService.getTop3TodayByViewCount();

			// then
			assertThat(results).hasSize(1);
			assertThat(results.get(0).viewCount()).isEqualTo(1000L);
			verify(webtoonRepository).findTop3TodayByViewCount();
		}
	}

	@Nested
	@DisplayName("최근 조회한 웹툰 조회 테스트")
	class GetRecentWebtoonsTest {

		@Test
		@DisplayName("사용자의 최근 조회한 웹툰 목록을 조회한다")
		void getRecentWebtoons_Success() {
			// given
			Long userId = 1L;
			List<Webtoon> recentWebtoons = createRecentWebtoonList();

			given(recentViewRepository.findRecentWebtoonsByUserId(userId, 4))
				.willReturn(recentWebtoons);

			// when
			List<WebtoonCardDto> results = webtoonService.getRecentWebtoons(userId);

			// then
			assertThat(results).hasSize(recentWebtoons.size());

			// 첫 번째 결과의 변환 검증
			WebtoonCardDto firstResult = results.get(0);
			Webtoon firstWebtoon = recentWebtoons.get(0);
			assertThat(firstResult.title()).isEqualTo(firstWebtoon.getTitle());
			assertThat(firstResult.id()).isEqualTo(firstWebtoon.getId());

			verify(recentViewRepository).findRecentWebtoonsByUserId(userId, 4);
		}

		@Test
		@DisplayName("최근 조회한 웹툰이 없는 경우 빈 리스트를 반환한다")
		void getRecentWebtoons_Empty() {
			// given
			Long userId = 1L;

			given(recentViewRepository.findRecentWebtoonsByUserId(userId, 4))
				.willReturn(List.of());

			// when
			List<WebtoonCardDto> results = webtoonService.getRecentWebtoons(userId);

			// then
			assertThat(results).isEmpty();
			verify(recentViewRepository).findRecentWebtoonsByUserId(userId, 4);
		}
	}

	@Nested
	@DisplayName("카테고리별 웹툰 조회 (제한) 테스트")
	class GetWebtoonsByCategoryLimit3Test {

		@Test
		@DisplayName("각 카테고리별로 최대 3개씩 웹툰을 조회한다")
		void getWebtoonsByCategoryLimit3_Success() {
			// given
			List<Webtoon> itWebtoons = createItWebtoons(3);
			List<Webtoon> financeWebtoons = createFinanceWebtoons(2);
			List<Webtoon> politicsWebtoons = createPoliticsWebtoons(1);

			given(webtoonRepository.findTop3ByCategoryOrderByCreatedAtDesc(Category.IT))
				.willReturn(itWebtoons);
			given(webtoonRepository.findTop3ByCategoryOrderByCreatedAtDesc(Category.FINANCE))
				.willReturn(financeWebtoons);
			given(webtoonRepository.findTop3ByCategoryOrderByCreatedAtDesc(Category.POLITICS))
				.willReturn(politicsWebtoons);

			// when
			Map<String, List<WebtoonCardDto>> results = webtoonService.getWebtoonsByCategoryLimit3();

			// then
			assertThat(results).hasSize(3);
			assertThat(results.get("IT")).hasSize(3);
			assertThat(results.get("FINANCE")).hasSize(2);
			assertThat(results.get("POLITICS")).hasSize(1);

			verify(webtoonRepository).findTop3ByCategoryOrderByCreatedAtDesc(Category.IT);
			verify(webtoonRepository).findTop3ByCategoryOrderByCreatedAtDesc(Category.FINANCE);
			verify(webtoonRepository).findTop3ByCategoryOrderByCreatedAtDesc(Category.POLITICS);
		}
	}

	@Test
	@DisplayName("이미 북마크 되어 있으면 삭제 후 false 반환")
	void testToggleBookmark_removeExisting() {
		// given
		Long webtoonId = 1L;
		Long userId = 2L;
		WebtoonFavorite existing = new WebtoonFavorite(new User(userId), new Webtoon(webtoonId));

		given(webtoonFavoriteRepository.findByWebtoonIdAndUserId(webtoonId, userId))
			.willReturn(Optional.of(existing));

		// when
		boolean result = webtoonService.toggleBookmark(webtoonId, userId);

		// then
		then(webtoonFavoriteRepository).should().delete(existing);
		assertThat(result).isFalse();
	}

	@Test
	@DisplayName("북마크가 없으면 추가 후 true 반환")
	void testToggleBookmark_addNew() {
		// given
		Long webtoonId = 1L;
		Long userId = 3L;
		Webtoon webtoon = new Webtoon(webtoonId);

		given(webtoonFavoriteRepository.findByWebtoonIdAndUserId(webtoonId, userId))
			.willReturn(Optional.empty());
		given(webtoonRepository.findById(webtoonId))
			.willReturn(Optional.of(webtoon));

		// when
		boolean result = webtoonService.toggleBookmark(webtoonId, userId);

		// then
		then(webtoonFavoriteRepository).should().save(any(WebtoonFavorite.class));
		assertThat(result).isTrue();
	}
}
