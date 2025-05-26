package com.akatsuki.newsum.domain.webtoon.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.akatsuki.newsum.domain.user.entity.User;
import com.akatsuki.newsum.domain.webtoon.entity.webtoon.Webtoon;
import com.akatsuki.newsum.domain.webtoon.entity.webtoon.WebtoonFavorite;
import com.akatsuki.newsum.domain.webtoon.repository.WebtoonFavoriteRepository;
import com.akatsuki.newsum.domain.webtoon.repository.WebtoonRepository;

@ExtendWith(MockitoExtension.class)
class WebtoonServiceTest {

	@Mock
	private WebtoonFavoriteRepository webtoonFavoriteRepository;

	@Mock
	private WebtoonRepository webtoonRepository;

	@InjectMocks
	private WebtoonService webtoonService;

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
