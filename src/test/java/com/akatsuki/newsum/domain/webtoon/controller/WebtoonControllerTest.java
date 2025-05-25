package com.akatsuki.newsum.domain.webtoon.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.akatsuki.newsum.common.security.TokenProvider;
import com.akatsuki.newsum.common.security.UserDetailsImpl;
import com.akatsuki.newsum.domain.user.entity.User;
import com.akatsuki.newsum.domain.webtoon.dto.WebtoonLikeStatusDto;
import com.akatsuki.newsum.domain.webtoon.service.WebtoonService;

@WebMvcTest(WebtoonController.class)
public class WebtoonControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private WebtoonService webtoonService;

	@MockBean
	private TokenProvider tokenProvider;

	@Test
	@DisplayName("좋아요한 상태의 유저가 좋아요 토글 API 호출 시 liked: true 반환")
	@WithMockUser
	void toggleWebtoonLike_alreadyLiked_returnsTrue() throws Exception {
		// given
		Long webtoonId = 1L;

		User mockUser = User.builder()
			.email("test@example.com")
			.nickname("testuser")
			.profileImageUrl("https://example.com/profile.jpg")
			.build();
		UserDetailsImpl userDetails = new UserDetailsImpl(mockUser);

		// Spring Security Context 설정
		Authentication auth = mock(Authentication.class);
		when(auth.getPrincipal()).thenReturn(userDetails);
		SecurityContext context = mock(SecurityContext.class);
		when(context.getAuthentication()).thenReturn(auth);
		SecurityContextHolder.setContext(context);

		// 좋아요 상태 mock 설정
		WebtoonLikeStatusDto likeStatus = new WebtoonLikeStatusDto(true, 10);

		// when & then
		mockMvc.perform(post("/api/v1/webtoons/{webtoonId}/likes", webtoonId))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data.liked").value(true));
	}
}
