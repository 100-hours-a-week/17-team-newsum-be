package com.akatsuki.newsum.domain.webtoon.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
import com.akatsuki.newsum.common.security.UserDetailsImpl;
import com.akatsuki.newsum.domain.webtoon.dto.CommentAndSubComments;
import com.akatsuki.newsum.domain.webtoon.dto.CommentCreateRequest;
import com.akatsuki.newsum.domain.webtoon.dto.CommentEditRequest;
import com.akatsuki.newsum.domain.webtoon.dto.CommentListResponse;
import com.akatsuki.newsum.domain.webtoon.dto.CommentListResult;
import com.akatsuki.newsum.domain.webtoon.service.CommentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/webtoons")
@RequiredArgsConstructor
public class CommentController {

	private final CommentService commentService;
	private final CursorPaginationService cursorPaginationService;

	@GetMapping("/{webtoonId}/comments")
	public ResponseEntity<ApiResponse<CommentListResponse>> getComments(
		@PathVariable Long webtoonId,
		@CursorParam(cursorType = CreatedAtIdCursor.class) Cursor cursor,
		@RequestParam(defaultValue = "10", required = false) Integer size,
		@AuthenticationPrincipal UserDetailsImpl userDetails
	) {
		Long id = getUserId(userDetails);

		CommentListResult result = commentService.findCommentsByWebtoon(webtoonId, cursor, size,
			id);
		CursorPage<CommentAndSubComments> cursorPage = cursorPaginationService.create(
			result.comments(),
			size,
			cursor);

		CommentListResponse response = CommentListResponse.of(cursorPage, result.commentCount());
		return ResponseEntity.ok(
			ApiResponse.success(ResponseCodeAndMessage.COMMENT_FIND_SUCCESS, response)
		);
	}

	@PostMapping("/{webtoonId}/comments")
	public ResponseEntity<ApiResponse<CommentListResponse>> createComment(
		@PathVariable Long webtoonId,
		@RequestBody CommentCreateRequest request,
		@AuthenticationPrincipal UserDetailsImpl userDetails
	) {
		Long id = getUserId(userDetails);

		commentService.addComment(request, webtoonId, id);
		return ResponseEntity.ok(
			ApiResponse.success(ResponseCodeAndMessage.COMMENT_ADD_SUCCESS, null)
		);
	}

	@PatchMapping("/{webtoonId}/comments/{commentId}")
	public ResponseEntity<ApiResponse<CommentListResponse>> editComment(
		@PathVariable Long webtoonId,
		@PathVariable Long commentId,
		@RequestBody CommentEditRequest request,
		@AuthenticationPrincipal UserDetailsImpl userDetails
	) {
		Long id = getUserId(userDetails);

		commentService.editComment(request, webtoonId, commentId, id);
		return ResponseEntity.ok(
			ApiResponse.success(ResponseCodeAndMessage.COMMEND_EDIT_SUCCESS, null)
		);
	}

	@DeleteMapping("/{webtoonId}/comments/{commentId}")
	public ResponseEntity<ApiResponse<CommentListResponse>> deleteComment(
		@PathVariable Long webtoonId,
		@PathVariable Long commentId,
		@AuthenticationPrincipal UserDetailsImpl userDetails
	) {
		Long id = getUserId(userDetails);

		commentService.deleteComment(webtoonId, commentId, id);
		return ResponseEntity.ok(
			ApiResponse.success(ResponseCodeAndMessage.COMMENT_DELETE_SUCCESS, null)
		);
	}

	private Long getUserId(
		UserDetailsImpl userDetails) {
		if (userDetails == null) {
			return null;
		}
		return userDetails.getUserId();
	}

	@PostMapping("/{webtoonId}/comments/{commentId}/likes")
	public ResponseEntity<ApiResponse<CommentListResponse>> likeComment(
		@PathVariable Long webtoonId,
		@PathVariable Long commentId,
		@AuthenticationPrincipal UserDetailsImpl userDetails
	) {
		Long id = getUserId(userDetails);

		boolean result = commentService.toggleCommentLike(commentId, id);
		return ResponseEntity.ok(
			ApiResponse.success(ResponseCodeAndMessage.ARTICLE_LIKE_TOGGLE_SUCCESS, null));
	}
}
