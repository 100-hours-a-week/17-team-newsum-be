package com.akatsuki.newsum.domain.webtoon.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.akatsuki.newsum.common.security.UserDetailsImpl;
import com.akatsuki.newsum.domain.user.entity.User;
import com.akatsuki.newsum.domain.webtoon.service.WebtoonService;

@WebMvcTest(WebtoonController.class)

public class WebtoonControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private WebtoonService webtoonService;

	@Test
	@DisplayName("웹툰 좋아요 토글 - 성공")
	@WithMockUser
		// 시큐리티 우회용
	void toggleWebtoonLike_success() throws Exception {
		// given
		Long webtoonId = 1L;
		User mockUser = User.builder()
			.email("test@example.com")
			.nickname("testuser")
			.profileImageUrl("https://example.com/profile.jpg")
			.build();
		UserDetailsImpl userDetails = new UserDetailsImpl(mockUser);

		// Spring Security Context에 유저 설정
		Authentication auth = Mockito.mock(Authentication.class);
		Mockito.when(auth.getPrincipal()).thenReturn(userDetails);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(auth);
		SecurityContextHolder.setContext(securityContext);

		// when & then
		mockMvc.perform(post("/api/v1/webtoons/{webtoonId}/like", webtoonId))
			.andExpect(status().isOk());
	}
}
