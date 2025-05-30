package com.akatsuki.newsum.domain.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.akatsuki.newsum.common.security.TokenResponse;
import com.akatsuki.newsum.domain.user.dto.CodeRequestDto;
import com.akatsuki.newsum.domain.user.service.GoogleOAuthService;
import com.akatsuki.newsum.domain.user.service.KakaoOAuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/oauth2")
public class OAuthLoginController {

	private final GoogleOAuthService googleOAuthService;
	private final KakaoOAuthService kakaoOAuthService;

	@PostMapping("/google/callback")
	public ResponseEntity<TokenResponse> googleLogin(@RequestBody CodeRequestDto request) {
		String code = request.getCode();
		TokenResponse token = googleOAuthService.loginWithCode(code);
		return ResponseEntity.ok(token);
	}

	@PostMapping("/kakao/callback")
	public ResponseEntity<TokenResponse> kakaoLogin(@RequestBody CodeRequestDto request) {
		String code = request.getCode();
		TokenResponse token = kakaoOAuthService.loginWithCode(code);
		return ResponseEntity.ok(token);
	}

}
