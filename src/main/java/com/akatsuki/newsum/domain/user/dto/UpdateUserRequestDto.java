package com.akatsuki.newsum.domain.user.dto;

public record UpdateUserRequestDto(
	String nickname,
	String profileimage
) {
}
