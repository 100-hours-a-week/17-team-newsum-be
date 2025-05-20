package com.akatsuki.newsum.domain.webtoon.controller;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import com.akatsuki.newsum.common.dto.ResponseCodeAndMessage;
import com.akatsuki.newsum.domain.webtoon.service.WebtoonService;

@TestPropertySource(properties = "spring.profiles.active=test")
@ActiveProfiles("test")
@WebMvcTest(WebtoonController.class)
public class WebtoonControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private WebtoonService webtoonService;

	@Test
	@WithMockUser
	@DisplayName("북마크 토클 api 호출 시 200 과 true/false 반환")
	void toggleFavotire_bookmarkadd() throws Exception {
		//given
		Long webtoonId = 1L;
		Long userId = 1L;
		boolean favotire = true; //북마크 추가

		given(webtoonService.toggleBookmark(webtoonId, userId))
			.willReturn(favotire);

		//when + then
		mockMvc.perform(post("/api/v1/webtoons/{webtoonId}/favorites", webtoonId)
				.principal(() -> userId.toString()))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.code").value(ResponseCodeAndMessage.WEBTOON_BOOKMARK_SUCCESS.getCode()))
			.andExpect(jsonPath("$.data").value(favotire));
	}

}
