package com.akatsuki.newsum.domain.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.akatsuki.newsum.common.security.UserDetailsImpl;
import com.akatsuki.newsum.domain.user.dto.UserProfileDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

	@GetMapping("/profile")
	public ResponseEntity<UserProfileDto> getProfile(@AuthenticationPrincipal UserDetailsImpl userDetails) {
		var user = userDetails.getUser();

		UserProfileDto dto = new UserProfileDto(
			user.getEmail(),
			user.getNickname(),
			user.getProfileImageUrl()
		);

		return ResponseEntity.ok(dto);
	}
}
