package com.akatsuki.newsum.fixture.dto;

import java.util.List;

import com.akatsuki.newsum.domain.webtoon.dto.CreateWebtoonReqeust;
import com.akatsuki.newsum.domain.webtoon.dto.NewsSourceDto;
import com.akatsuki.newsum.domain.webtoon.dto.WebtoonSlideDto;

public class WebtoonDtoFixture {

	public static final String VALID_SEARCH_QUERY = "IT";
	public static final String EMPTY_SEARCH_QUERY = "존재하지않는검색어";
	public static final String IT_CATEGORY = "IT";
	public static final String FINANCE_CATEGORY = "FINANCE";
	public static final String POLITICS_CATEGORY = "POLITICS";
	public static final Long EXISTING_WEBTOON_ID = 1L;
	public static final Long NON_EXISTENT_WEBTOON_ID = 999L;

	// ========== 웹툰 생성 요청 픽스처 ==========

	public static CreateWebtoonReqeust createItWebtoonRequest() {
		return new CreateWebtoonReqeust(
			1L,
			"IT",
			"새로운 IT 웹툰",
			"IT에 대한 흥미로운 웹툰입니다",
			"https://example.com/it-webtoon.jpg",
			createDefaultSlides(),
			createDefaultNewsSources()
		);
	}

	public static CreateWebtoonReqeust createFinanceWebtoonRequest() {
		return new CreateWebtoonReqeust(
			1L,
			"FINANCE",
			"금융 웹툰",
			"금융에 대한 유익한 웹툰입니다",
			"https://example.com/finance-webtoon.jpg",
			List.of(
				new WebtoonSlideDto((byte)1, "https://example.com/finance-slide1.jpg", "금융 첫 번째 슬라이드")
			),
			List.of(
				new NewsSourceDto("금융 뉴스", "https://finance-news.example.com")
			)
		);
	}

	public static CreateWebtoonReqeust createPoliticsWebtoonRequest() {
		return new CreateWebtoonReqeust(
			1L,
			"POLITICS",
			"정치 웹툰",
			"정치에 대한 객관적인 웹툰입니다",
			"https://example.com/politics-webtoon.jpg",
			createDefaultSlides(),
			List.of(
				new NewsSourceDto("정치 뉴스 1", "https://politics-news1.example.com"),
				new NewsSourceDto("정치 뉴스 2", "https://politics-news2.example.com")
			)
		);
	}

	public static CreateWebtoonReqeust createInvalidCategoryWebtoonRequest() {
		return new CreateWebtoonReqeust(
			1L,
			"INVALID_CATEGORY",
			"테스트 웹툰",
			"테스트 내용",
			"https://example.com/image.jpg",
			createDefaultSlides(),
			createDefaultNewsSources()
		);
	}

	// ========== 공통 픽스처 메서드 ==========

	private static List<WebtoonSlideDto> createDefaultSlides() {
		return List.of(
			new WebtoonSlideDto((byte)1, "https://example.com/slide1.jpg", "첫 번째 슬라이드"),
			new WebtoonSlideDto((byte)2, "https://example.com/slide2.jpg", "두 번째 슬라이드"),
			new WebtoonSlideDto((byte)3, "https://example.com/slide3.jpg", "세 번째 슬라이드"),
			new WebtoonSlideDto((byte)4, "https://example.com/slide4.jpg", "네 번째 슬라이드")
		);
	}

	private static List<NewsSourceDto> createDefaultNewsSources() {
		return List.of(
			new NewsSourceDto("뉴스 헤드라인 1", "https://news1.example.com"),
			new NewsSourceDto("뉴스 헤드라인 2", "https://news2.example.com")
		);
	}
}
