package com.akatsuki.newsum.domain.webtoon.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import com.akatsuki.newsum.domain.user.entity.Status;
import com.akatsuki.newsum.domain.user.entity.User;
import com.akatsuki.newsum.domain.user.entity.UserRole;
import com.akatsuki.newsum.domain.user.repository.UserRepository;
import com.akatsuki.newsum.domain.webtoon.dto.WebtoonLikeStatusDto;
import com.akatsuki.newsum.domain.webtoon.entity.webtoon.Webtoon;
import com.akatsuki.newsum.domain.webtoon.entity.webtoon.WebtoonLike;
import com.akatsuki.newsum.domain.webtoon.repository.WebtoonLikeRepository;
import com.akatsuki.newsum.domain.webtoon.repository.WebtoonRepository;

public class WebtoonServiceTest {

	@InjectMocks
	private WebtoonService webtoonService;

	@Mock
	private WebtoonRepository webtoonRepository;

	@Mock
	private UserRepository userRepository;

	@Mock
	private WebtoonLikeRepository webtoonLikeRepository;

	private final Long webtoonId = 1L;
	private final Long userId = 100L;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	@DisplayName("사용자가 이미 좋아요한 경우 true 반환")
	void getWebtoonLikeStatus_whenAlreadyLiked_returnsTrue() {
		// given
		Webtoon webtoon = new Webtoon(webtoonId);
		User user = new User("test@example.com", "tester", "https://img", UserRole.USER_BASIC, Status.ACTIVATE);

		ReflectionTestUtils.setField(user, "id", userId);
		ReflectionTestUtils.setField(webtoon, "id", webtoonId);

		WebtoonLike like = new WebtoonLike(user, webtoon);

		when(webtoonLikeRepository.existsByWebtoonIdAndUserId(webtoonId, userId)).thenReturn(true);
		when(webtoonLikeRepository.countByWebtoonId(webtoonId)).thenReturn(10L);
		when(webtoonLikeRepository.findByWebtoonIdAndUserId(webtoonId, userId)).thenReturn(Optional.of(like));

		// when
		WebtoonLikeStatusDto result = webtoonService.getWebtoonLikeStatus(webtoonId, userId);

		// then
		assertThat(result.liked()).isTrue();
	}
}
