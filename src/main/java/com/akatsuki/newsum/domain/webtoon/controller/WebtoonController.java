package com.akatsuki.newsum.domain.webtoon.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.akatsuki.newsum.common.dto.ApiResponse;
import com.akatsuki.newsum.common.dto.ResponseCodeAndMessage;
import com.akatsuki.newsum.common.security.JwtTokenUtil;
import com.akatsuki.newsum.common.security.TokenProvider;
import com.akatsuki.newsum.domain.webtoon.dto.WebtoonDetailResponse;
import com.akatsuki.newsum.domain.webtoon.dto.WebtoonResponse;
import com.akatsuki.newsum.domain.webtoon.service.WebtoonService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/webtoons")
@RequiredArgsConstructor
public class WebtoonController {

	private final WebtoonService webtoonService;
	private final TokenProvider tokenProvider;

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
}
