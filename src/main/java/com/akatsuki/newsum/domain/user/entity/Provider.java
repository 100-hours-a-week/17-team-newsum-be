package com.akatsuki.newsum.domain.user.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Provider {
	KAKAO("kakao_account", "id", "email"),
	GOOGLE(null, "id", "email");

	/** JSON 최상위 키 (null이면 루트를 바로 사용) */
	private final String attributeRootKey;
	/** 사용자 식별자 필드명 (id 필드) */
	private final String providerCode;
	/** 이메일 등 주요 식별 필드명 */
	private final String providerEmail;
}
