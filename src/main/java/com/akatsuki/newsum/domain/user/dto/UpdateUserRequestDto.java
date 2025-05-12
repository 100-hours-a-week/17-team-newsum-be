package com.akatsuki.newsum.domain.user.dto;

import jakarta.validation.constraints.Size;

public record UpdateUserRequestDto(
	@Size(min = 1, max = 16)
	String nickname,
	String profileImage
) {
}
