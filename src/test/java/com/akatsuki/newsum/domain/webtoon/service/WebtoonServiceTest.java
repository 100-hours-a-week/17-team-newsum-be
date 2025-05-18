package com.akatsuki.newsum.domain.webtoon.service;

import static org.assertj.core.api.Assertions.*;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.akatsuki.newsum.cache.RedisService;
import com.akatsuki.newsum.domain.webtoon.dto.WebtoonLikeStatusDto;

public class WebtoonServiceTest {

	private WebtoonService webtoonService;
	private RedisService redisService;

	private final Long webtoonId = 1L;
	private final Long userId = 100L;

	@BeforeEach
	void setUp() {
		redisService = Mockito.mock(RedisService.class);

		webtoonService = new WebtoonService(
			null, null, null, null, null, null, null, redisService
		);
	}

	@Test
	@DisplayName("좋아요 토글 동작 테스트")
	void toggleLike() {
		// given: Redis Set이 비어있다고 가정
		Mockito.when(redisService.getSetMembers("webtoon:likes:1")).thenReturn(Set.of());

		// when: 좋아요 토글 (Set에 추가될 상황)
		webtoonService.toggleWebtoonLike(webtoonId, userId);

		// then: redisService.addSetValue()가 호출되었는지 확인
		Mockito.verify(redisService).addSetValue("webtoon:likes:1", userId);
		System.out.println("테스트완료 : 좋아요를 안 누른 경우");

	}

	@Test
	@DisplayName("좋아요 상태 조회 테스트 - 좋아요 안 누른 경우")
	void likeStatusFalse() {
		// given: userId가 포함되어 있지 않음
		Mockito.when(redisService.getSetMembers("webtoon:likes:1")).thenReturn(Set.of());

		// when
		WebtoonLikeStatusDto dto = webtoonService.getWebtoonLikeStatus(webtoonId, userId);

		// then
		assertThat(dto.liked()).isFalse();
	}

	@Test
	@DisplayName("좋아요 상태 조회 테스트 - 좋아요 누른 경우")
	void likeStatusTrue() {
		// given: userId가 포함된 상태
		Mockito.when(redisService.getSetMembers("webtoon:likes:1")).thenReturn(Set.of(userId));

		// when
		WebtoonLikeStatusDto dto = webtoonService.getWebtoonLikeStatus(webtoonId, userId);

		// then : 값이 True 로 출력되어야 한다.
		assertThat(dto.liked()).isTrue();
		System.out.println("True로 출력 : " + dto.liked());
	}

	@Test
	@DisplayName("좋아요 개수 확인")
	void likeCountTest() {
		//given : redis 에서 3명의 유저가 좋아요를 누른 상태
		Mockito.when(redisService.getSetMembers("webtoon:likes:1"))
			.thenReturn(Set.of(1L, 2L, 3L));

		//when : 좋아요 상태 조회
		WebtoonLikeStatusDto dto = webtoonService.getWebtoonLikeStatus(webtoonId, userId);

		//then : count 가 3개가 출력되어야 한다.
		assertThat(dto.likeCount()).isEqualTo(3);

		System.out.println(" 테스트완료: 좋아요 수 = " + dto.likeCount());
	}
}

