package com.akatsuki.newsum.domain.webtoon.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.akatsuki.newsum.common.dto.ApiResponse;
import com.akatsuki.newsum.common.dto.ResponseCodeAndMessage;
import com.akatsuki.newsum.common.pagination.CursorPaginationService;
import com.akatsuki.newsum.common.pagination.annotation.CursorParam;
import com.akatsuki.newsum.common.pagination.model.cursor.CreatedAtIdCursor;
import com.akatsuki.newsum.common.pagination.model.cursor.Cursor;
import com.akatsuki.newsum.common.pagination.model.page.CursorPage;
import com.akatsuki.newsum.common.security.JwtTokenUtil;
import com.akatsuki.newsum.common.security.TokenProvider;
import com.akatsuki.newsum.domain.webtoon.dto.WebtoonCardDto;
import com.akatsuki.newsum.domain.webtoon.dto.WebtoonDetailResponse;
import com.akatsuki.newsum.domain.webtoon.dto.WebtoonListResponse;
import com.akatsuki.newsum.domain.webtoon.dto.WebtoonResponse;
import com.akatsuki.newsum.domain.webtoon.service.WebtoonService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/webtoons")
@RequiredArgsConstructor
public class WebtoonController {

	private final WebtoonService webtoonService;
	private final CursorPaginationService cursorPaginationService;
	private final TokenProvider tokenProvider;

	@GetMapping
	public ResponseEntity<ApiResponse<WebtoonListResponse>> getWebtoons(
		//TODO : 추후 키워드, AI작가로 조회 가능
		@RequestParam(required = false) String category,
		@CursorParam(cursorType = CreatedAtIdCursor.class) Cursor cursor,
		@RequestParam(defaultValue = "10") int size
	) {
		List<WebtoonCardDto> result = webtoonService.findWebtoonsByCategory(category, cursor, size);
		CursorPage<WebtoonCardDto> cursorPage = cursorPaginationService.create(result, size, cursor);
		WebtoonListResponse response = WebtoonListResponse.of(cursorPage);

		return ResponseEntity.ok(
			ApiResponse.success(ResponseCodeAndMessage.WEBTOON_LIST_SUCCESS, response)
		);
	}

	@GetMapping("/{webtoonId}")
	public ResponseEntity<ApiResponse<WebtoonResponse>> getWebtoon(
		@PathVariable Long webtoonId,
		@RequestHeader(value = "Authorization", required = false) String bearerToken) {

		Long id = validateTokenAndExtractPrincipal(bearerToken);

		WebtoonResponse response = webtoonService.getWebtoon(webtoonId, id);

		return ResponseEntity.ok(
			ApiResponse.success(ResponseCodeAndMessage.WEBTOON_BASE_INFO_SUCCESS, response)
		);
	}

	@GetMapping("/{webtoonId}/details")
	public ResponseEntity<ApiResponse<WebtoonDetailResponse>> getWebtoonDetails(
		@PathVariable Long webtoonId) {

		WebtoonDetailResponse response = webtoonService.getWebtoonDetail(webtoonId);

		return ResponseEntity.ok(
			ApiResponse.success(ResponseCodeAndMessage.WEBTOON_DETAIL_SUCCESS, response)
		);
	}

	private Long validateTokenAndExtractPrincipal(String bearerToken) {
		if (bearerToken == null) {
			return null;
		}
		String accessToken = JwtTokenUtil.parseBearerToken(bearerToken);
		if (tokenProvider.validateToken(accessToken)) {
			return Long.parseLong(tokenProvider.getPrincipal(accessToken));
		}
		return null;
	}

	//메인페이지
	@GetMapping("/top")
	public ResponseEntity<ApiResponse<Map<String, List<WebtoonCardDto>>>> getTop() {
		List<WebtoonCardDto> topToons = webtoonService.getTop3TodayWebtoons();
		List<WebtoonCardDto> newsCards = webtoonService.getTodayNewsCards();

		Map<String, List<WebtoonCardDto>> response = Map.of(
			"topToons", topToons,
			"todaysNews", newsCards
		);

		return ResponseEntity.ok(
			ApiResponse.success(ResponseCodeAndMessage.WEBTOON_TOP_SUCCESS, response)
		);
	}

	@GetMapping("/main")
	public ResponseEntity<ApiResponse<Map<String, List<WebtoonCardDto>>>> getMain() {
		Map<String, List<WebtoonCardDto>> webtoonsByCategory = webtoonService.getWebtoonsByCategoryLimit3();

		return ResponseEntity.ok(
			ApiResponse.success(ResponseCodeAndMessage.WEBTOON_MAIN_SUCCESS, Map.of(
				"webtoonsByCategory", webtoonsByCategory
			))
		);
	}

	@GetMapping("/recent")
	public ResponseEntity<ApiResponse<Map<String, List<WebtoonCardDto>>>> getRecentViewed(
		@RequestHeader("Authorization") String bearerToken) {

		Long userId = validateTokenAndExtractPrincipal(bearerToken);
		List<WebtoonCardDto> recentViewed = webtoonService.getRecentViewedWebtoons(userId);

		return ResponseEntity.ok(
			ApiResponse.success(ResponseCodeAndMessage.WEBTOON_RECENT_SUCCESS, Map.of(
				"recentWebtoons", recentViewed
			))
		);
	}

}
