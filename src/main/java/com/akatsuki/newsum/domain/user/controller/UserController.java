package com.akatsuki.newsum.domain.user.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.akatsuki.newsum.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {

	private final UserRepository userRepository;

	@GetMapping("/profile")
	public ResponseEntity<UserProfileDto> getProfile(@AuthenticationPrincipal UserDetailsImpl userDetails) {
		User user = userDetails.getUser();

		UserProfileDto dto = new UserProfileDto(
			user.getEmail(),
			user.getNickname(),
			user.getProfileImageUrl()
		);

		return ResponseEntity.ok(dto);
	}
}