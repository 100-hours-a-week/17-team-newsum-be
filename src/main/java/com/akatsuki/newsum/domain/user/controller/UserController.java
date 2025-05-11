package com.akatsuki.newsum.domain.user.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.akatsuki.newsum.common.dto.ApiResponse;
import com.akatsuki.newsum.common.dto.ResponseCodeAndMessage;
import com.akatsuki.newsum.common.security.UserDetailsImpl;
import com.akatsuki.newsum.domain.user.dto.RecentViewWebtoonListResponse;
import com.akatsuki.newsum.domain.user.dto.UpdateUserRequestDto;
import com.akatsuki.newsum.domain.user.dto.UpdateUserResponseDto;
import com.akatsuki.newsum.domain.user.dto.UserProfileDto;
import com.akatsuki.newsum.domain.user.service.UserService;
import com.akatsuki.newsum.domain.webtoon.dto.WebtoonCardDto;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

	private final UserService userService;

	@GetMapping("/profile")
	public ResponseEntity<ApiResponse<UserProfileDto>> getProfile(
		@AuthenticationPrincipal UserDetailsImpl userDetails) {
		var user = userDetails.getUser();

		UserProfileDto dto = new UserProfileDto(
			user.getEmail(),
			user.getNickname(),
			user.getProfileImageUrl(),
			String.valueOf(user.getId())
		);

		return ResponseEntity.ok(ApiResponse.success(ResponseCodeAndMessage.USER_INFO_SUCCESS, dto));
	}

	@GetMapping("/webtoons/recent")
	public ResponseEntity<ApiResponse<RecentViewWebtoonListResponse>> getRecentWebtoonList(
		@AuthenticationPrincipal UserDetailsImpl userDetails
	) {
		Long id = userDetails.getUser().getId();
		Map<String, List<WebtoonCardDto>> result = userService.findRecentWebtoonList(id);
		RecentViewWebtoonListResponse response = new RecentViewWebtoonListResponse(result);
		return ResponseEntity.ok(
			ApiResponse.success(ResponseCodeAndMessage.USER_RECENTLY_VIEWED_WEBTOON_LIST_SUCCESS, response));
	}

	@PatchMapping("/me")
	public ResponseEntity<ApiResponse<UpdateUserResponseDto>> updateMyProfile(
		@AuthenticationPrincipal UserDetailsImpl userDetails,
		@Valid @RequestBody UpdateUserRequestDto dto
	) {
		Long userId = userDetails.getUser().getId();
		UpdateUserResponseDto responseDto = userService.updateUser(userId, dto);

		return ResponseEntity.ok(
			ApiResponse.success(ResponseCodeAndMessage.USER_INFO_UPDATE_SUCCESS, responseDto)
		);
	}
}
