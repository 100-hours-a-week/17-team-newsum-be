package com.akatsuki.newsum.domain.user.dto;

import org.hibernate.validator.constraints.URL;

import jakarta.validation.constraints.Size;

public record UpdateUserRequestDto(
	@Size(min = 1, max = 16)
	String nickname,

	@URL
	String profileImage
) {
}
