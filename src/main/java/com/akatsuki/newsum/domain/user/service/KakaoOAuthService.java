package com.akatsuki.newsum.domain.user.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.akatsuki.newsum.common.security.TokenProvider;
import com.akatsuki.newsum.common.security.TokenResponse;
import com.akatsuki.newsum.domain.user.dto.KakaoUserInfo;
import com.akatsuki.newsum.domain.user.dto.OAuthUserInfo;
import com.akatsuki.newsum.domain.user.entity.Provider;
import com.akatsuki.newsum.domain.user.entity.SocialLogin;
import com.akatsuki.newsum.domain.user.entity.Status;
import com.akatsuki.newsum.domain.user.entity.User;
import com.akatsuki.newsum.domain.user.entity.UserRole;
import com.akatsuki.newsum.domain.user.repository.SocialLoginRepository;
import com.akatsuki.newsum.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KakaoOAuthService {
	private final UserRepository userRepository;
	private final SocialLoginRepository socialLoginRepository;
	private final TokenProvider tokenProvider;
	private final RestTemplate restTemplate = new RestTemplate();

	@Value("${oauth.kakao.client-id}")
	private String clientId;

	@Value("${oauth.kakao.client-secret:}")
	private String clientSecret;

	@Value("${oauth.kakao.redirect-uri}")
	private String redirectUri;

	public TokenResponse loginWithCode(String code) {
		String accessToken = getAccessToken(code);
		OAuthUserInfo userInfo = getUserInfo(accessToken);

		User user = userRepository.findByEmail(userInfo.getEmail())
			.orElseGet(() -> saveNewUser(userInfo));

		if (!socialLoginRepository.existsByUserAndProvider(user, Provider.KAKAO)) {
			SocialLogin socialLogin = new SocialLogin(user, userInfo.getId(), Provider.KAKAO);
			socialLoginRepository.save(socialLogin);
		}

		String accessJwt = tokenProvider.createAccessToken(user.getId(), user.getEmail(), user.getRole());
		String refreshJwt = tokenProvider.createRefreshToken();

		return new TokenResponse(accessJwt, refreshJwt, userInfo);
	}

	private String getAccessToken(String code) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("grant_type", "authorization_code");
		params.add("client_id", clientId);
		params.add("redirect_uri", redirectUri);
		params.add("code", code);
		params.add("client_secret", clientSecret);

		ResponseEntity<Map> response = restTemplate.exchange(
			"https://kauth.kakao.com/oauth/token",
			HttpMethod.POST,
			new HttpEntity<>(params, headers),
			Map.class
		);

		return (String)response.getBody().get("access_token");
	}

	private OAuthUserInfo getUserInfo(String accessToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "Bearer " + accessToken);

		ResponseEntity<Map> response = restTemplate.exchange(
			"https://kapi.kakao.com/v2/user/me",
			HttpMethod.GET,
			new HttpEntity<>(headers),
			Map.class
		);

		return new KakaoUserInfo(response.getBody());
	}

	private User saveNewUser(OAuthUserInfo userInfo) {
		return userRepository.save(User.builder()
			.email(userInfo.getEmail())
			.nickname(userInfo.getName())
			.profileImageUrl(userInfo.getPicture())
			.role(UserRole.USER_BASIC)
			.status(Status.ACTIVATE)
			.build());
	}
}
