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

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
			.authorizeHttpRequests(auth -> auth
				.requestMatchers("/", "/login/**", "/oauth2/**")
				.permitAll()
				.anyRequest()
				.authenticated()
			)
			.oauth2Login(oauth2 -> oauth2
				.successHandler(oAuth2LoginSuccessHandler) // 로그인 성공 시 핸들러
			);

		return http.build();
	}
}