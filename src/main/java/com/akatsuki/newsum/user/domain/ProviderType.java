package com.akatsuki.newsum.user.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ProviderType {
    KAKAO("kakao_account", "id", "email"),
    GOOGLE( null,        "sub","email"),
    NAVER(  "response",  "id", "email");

    /** JSON 최상위 키 (null이면 루트를 바로 사용) */
    private final String attributeRootKey;
    /** 사용자 식별자 필드명 (id 필드) */
    private final String providerCode;
    /** 이메일 등 주요 식별 필드명 */
    private final String providerEmail;


    public static ProviderType from(String provider) {
        return java.util.Arrays.stream(values())
                .filter(p -> p.name().equalsIgnoreCase(provider))
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException("지원하지 않는 프로바이더: " + provider)
                );
    }
}
