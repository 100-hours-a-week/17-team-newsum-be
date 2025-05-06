package com.akatsuki.newsum.domain.webtoon.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.akatsuki.newsum.common.dto.ApiResponse;
import com.akatsuki.newsum.common.dto.ResponseCodeAndMessage;
import com.akatsuki.newsum.common.pagination.CursorPaginationService;
import com.akatsuki.newsum.common.pagination.annotation.CursorParam;
import com.akatsuki.newsum.common.pagination.model.cursor.CreatedAtIdCursor;
import com.akatsuki.newsum.common.pagination.model.cursor.Cursor;
import com.akatsuki.newsum.common.pagination.model.page.CursorPage;
import com.akatsuki.newsum.common.security.JwtTokenUtil;
import com.akatsuki.newsum.common.security.TokenProvider;
import com.akatsuki.newsum.domain.webtoon.dto.CommentAndSubComments;
import com.akatsuki.newsum.domain.webtoon.dto.CommentCreateRequest;
import com.akatsuki.newsum.domain.webtoon.dto.CommentEditRequest;
import com.akatsuki.newsum.domain.webtoon.dto.CommentListResponse;
import com.akatsuki.newsum.domain.webtoon.service.CommentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/webtoons")
@RequiredArgsConstructor
public class CommentController {

	private final CommentService commentService;
	private final TokenProvider tokenProvider;
	private final CursorPaginationService cursorPaginationService;

	@GetMapping("/{webtoonId}/comments")
	public ResponseEntity<ApiResponse<CommentListResponse>> getComments(
		@PathVariable Long webtoonId,
		@CursorParam(cursorType = CreatedAtIdCursor.class) Cursor cursor,
		@RequestParam(defaultValue = "10", required = false) Integer size,
		@RequestHeader(value = "Authorization", required = false) String bearerToken
	) {
		Long id = validateTokenAndExtractPrincipal(bearerToken);
		List<CommentAndSubComments> commentsByWebtoon = commentService.findCommentsByWebtoon(webtoonId, cursor, size,
			id);
		CursorPage<CommentAndSubComments> cursorPage = cursorPaginationService.create(
			commentsByWebtoon,
			size,
			cursor);

		CommentListResponse response = CommentListResponse.of(cursorPage);
		return ResponseEntity.ok(
			ApiResponse.success(ResponseCodeAndMessage.COMMENT_FIND_SUCCESS, response)
		);
	}

	@PostMapping("/{webtoonId}/comments")
	public ResponseEntity<ApiResponse<CommentListResponse>> createComment(
		@PathVariable Long webtoonId,
		@RequestBody CommentCreateRequest request,
		@RequestHeader(value = "Authorization", required = false) String bearerToken
	) {
		// Long id = validateTokenAndExtractPrincipal(bearerToken);

		commentService.addComment(request, webtoonId, 1L);
		return ResponseEntity.ok(
			ApiResponse.success(ResponseCodeAndMessage.COMMENT_ADD_SUCCESS, null)
		);
	}

	@PatchMapping("/{webtoonId}/comments/{commentId}")
	public ResponseEntity<ApiResponse<CommentListResponse>> editComment(
		@PathVariable Long webtoonId,
		@PathVariable Long commentId,
		@RequestBody CommentEditRequest request,
		@RequestHeader(value = "Authorization", required = false) String bearerToken
	) {
		// Long id = validateTokenAndExtractPrincipal(bearerToken);

		commentService.editComment(request, webtoonId, commentId, 1L);
		return ResponseEntity.ok(
			ApiResponse.success(ResponseCodeAndMessage.COMMEND_EDIT_SUCCESS, null)
		);
	}

	@DeleteMapping("/{webtoonId}/comments/{commentId}")
	public ResponseEntity<ApiResponse<CommentListResponse>> deleteComment(
		@PathVariable Long webtoonId,
		@PathVariable Long commentId,
		@RequestHeader(value = "Authorization", required = false) String bearerToken
	) {
		// Long id = validateTokenAndExtractPrincipal(bearerToken);

		commentService.deleteComment(webtoonId, commentId, 1L);
		return ResponseEntity.ok(
			ApiResponse.success(ResponseCodeAndMessage.COMMENT_DELETE_SUCCESS, null)
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
