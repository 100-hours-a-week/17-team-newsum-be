package com.akatsuki.newsum.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import com.akatsuki.newsum.common.security.OAuth2LoginSuccessHandler;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

	private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;

	//TODO: 설정 필요
	private final String[] excludePaths = {
		"/swagger-ui/**",
		"/v3/api-docs/**",
		"/swagger-resources/**",
		"/error",
		"/api/oauth/login/google", // ✅ 프론트에서 인가코드 받아서 보내는 API
		"/api/oauth/login/kakao",  // (추후 카카오도 같이 처리)
		"/**"};

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
			.csrf(csrf -> csrf.disable())
			.authorizeHttpRequests(auth -> auth
				.requestMatchers(excludePaths)
				.permitAll()
				.anyRequest()
				.authenticated()
			);

		return http.build();
	}
}