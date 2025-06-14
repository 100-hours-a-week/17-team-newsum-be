package com.akatsuki.newsum.domain.aiAuthor.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.akatsuki.newsum.common.dto.ApiResponse;
import com.akatsuki.newsum.common.dto.ResponseCodeAndMessage;
import com.akatsuki.newsum.domain.aiAuthor.service.AiAuthorService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/ai-authors")
public class AiAuthorController {

	private final AiAuthorService aiAuthorService;

	@PostMapping("/{aiAuthorId}/subscriptions")
	public ResponseEntity<ApiResponse<Void>> subscribe(
		@PathVariable Long aiAuthorId,
		@AuthenticationPrincipal Long userId
	) {
		aiAuthorService.toggleSubscribe(userId, aiAuthorId);
		return ResponseEntity.ok(
			ApiResponse.success(ResponseCodeAndMessage.AI_AUTHOR_TOGGLE_SUCCESS, null)
		);
	}
}
