package com.akatsuki.newsum.fixture.entity;

import static com.akatsuki.newsum.fixture.entity.AiAuthorFixture.*;

import java.util.List;

import com.akatsuki.newsum.domain.webtoon.entity.webtoon.Category;
import com.akatsuki.newsum.domain.webtoon.entity.webtoon.Webtoon;

/**
 * 웹툰 도메인 테스트를 위한 픽스처 클래스
 */
public class WebtoonDomainFixture {

	// ========== 웹툰 리스트 생성 ==========

	public static List<Webtoon> createWebtoonList(int count) {
		return List.of(
			createWebtoonWithTitle("테스트 웹툰 1"),
			createWebtoonWithTitle("테스트 웹툰 2")
		).subList(0, Math.min(count, 2));
	}

	public static List<Webtoon> createRecentWebtoonList() {
		return List.of(
			createWebtoonWithCategory("최근 조회 웹툰 1", Category.IT),
			createWebtoonWithCategory("최근 조회 웹툰 2", Category.FINANCE),
			createWebtoonWithCategory("최근 조회 웹툰 3", Category.POLITICS)
		);
	}

	// ========== 개별 웹툰 생성 ==========

	public static Webtoon createWebtoonWithTitle(String title) {
		return new Webtoon(
			DEFAULT_AI_AUTHOR,
			Category.IT,
			title,
			"테스트 웹툰 내용",
			"https://example.com/thumbnail.jpg"
		);
	}

	public static Webtoon createWebtoonWithCategory(String title, Category category) {
		return new Webtoon(
			DEFAULT_AI_AUTHOR,
			category,
			title,
			"테스트 웹툰 내용",
			"https://example.com/thumbnail.jpg"
		);
	}

	public static Webtoon createWebtoonWithViewCount(String title, Long viewCount) {
		Webtoon webtoon = new Webtoon(
			DEFAULT_AI_AUTHOR,
			Category.IT,
			title,
			"테스트 웹툰 내용",
			"https://example.com/thumbnail.jpg"
		);

		// 조회수 설정을 위해 해당 횟수만큼 증가
		for (int i = 0; i < viewCount; i++) {
			webtoon.increaseViewCount();
		}

		return webtoon;
	}

	// ========== 특정 카테고리별 웹툰 생성 ==========

	public static List<Webtoon> createItWebtoons(int count) {
		return createWebtoonsWithCategory(Category.IT, count);
	}

	public static List<Webtoon> createFinanceWebtoons(int count) {
		return createWebtoonsWithCategory(Category.FINANCE, count);
	}

	public static List<Webtoon> createPoliticsWebtoons(int count) {
		return createWebtoonsWithCategory(Category.POLITICS, count);
	}

	private static List<Webtoon> createWebtoonsWithCategory(Category category, int count) {
		return java.util.stream.IntStream.range(1, count + 1)
			.mapToObj(i -> new Webtoon(
				DEFAULT_AI_AUTHOR,
				category,
				category.name() + " 웹툰 " + i,
				category.name() + " 관련 웹툰 내용입니다.",
				"https://example.com/" + category.name().toLowerCase() + i + ".jpg"
			))
			.toList();
	}
}
