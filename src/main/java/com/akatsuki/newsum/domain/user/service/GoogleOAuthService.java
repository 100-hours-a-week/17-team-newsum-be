package com.akatsuki.newsum.domain.user.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.akatsuki.newsum.common.security.TokenProvider;
import com.akatsuki.newsum.common.security.TokenResponse;
import com.akatsuki.newsum.domain.user.dto.GoogleUserInfo;
import com.akatsuki.newsum.domain.user.entity.Provider;
import com.akatsuki.newsum.domain.user.entity.SocialLogin;
import com.akatsuki.newsum.domain.user.entity.User;
import com.akatsuki.newsum.domain.user.entity.UserRole;
import com.akatsuki.newsum.domain.user.repository.SocialLoginRepository;
import com.akatsuki.newsum.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GoogleOAuthService {

	private final UserRepository userRepository;
	private final SocialLoginRepository socialLoginRepository;
	private final TokenProvider tokenProvider;

	@Value("${oauth.google.client-id}")
	private String clientId;

	@Value("${oauth.google.client-secret}")
	private String clientSecret;

	@Value("${oauth.google.redirect-uri}")
	private String redirectUri;

	private final RestTemplate restTemplate = new RestTemplate();

	public TokenResponse loginWithCode(String code) {
		// 1. access token 요청
		String accessToken = getAccessToken(code);

		// 2. 사용자 정보 요청
		GoogleUserInfo userInfo = getUserInfo(accessToken);

		// 3. User 조회 또는 생성
		User user = userRepository.findByEmail(userInfo.getEmail())
			.orElseGet(() -> saveNewUser(userInfo));

		// 4. SocialLogin 등록 (없으면)
		if (!socialLoginRepository.existsByUserAndProvider(user, Provider.GOOGLE)) {
			String googleId = userInfo.getId(); // 구글 유저 고유 ID (문자열)
			Long providerId = Long.parseLong(googleId); // 💡 Long으로 변환

			SocialLogin socialLogin = new SocialLogin(user, providerId, Provider.GOOGLE);
			socialLoginRepository.save(socialLogin);
		}

		// 5. JWT 발급
		String accessJwt = tokenProvider.createAccessToken(user.getId(), user.getEmail(), user.getRole());
		String refreshJwt = tokenProvider.createRefreshToken();

		return new TokenResponse(accessJwt, refreshJwt);
	}

	private String getAccessToken(String code) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		HttpEntity<Map<String, String>> request = new HttpEntity<>(Map.of(
			"code", code,
			"client_id", clientId,
			"client_secret", clientSecret,
			"redirect_uri", redirectUri,
			"grant_type", "authorization_code"
		), headers);

		ResponseEntity<Map> response = restTemplate.exchange(
			"https://oauth2.googleapis.com/token",
			HttpMethod.POST,
			request,
			Map.class
		);

		return (String)response.getBody().get("access_token");
	}

	private GoogleUserInfo getUserInfo(String accessToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "Bearer " + accessToken);

		ResponseEntity<Map> response = restTemplate.exchange(
			"https://www.googleapis.com/oauth2/v2/userinfo",
			HttpMethod.GET,
			new HttpEntity<>(headers),
			Map.class
		);

		Map<String, Object> body = response.getBody();
		return new GoogleUserInfo(
			(String)body.get("email"),
			(String)body.get("name"),
			(String)body.get("picture"),
			(String)body.get("id")        // ✅ 이 줄이 추가되어야 합니다
		);
	}

	private User saveNewUser(GoogleUserInfo userInfo) {
		return userRepository.save(User.builder()
			.email(userInfo.getEmail())
			.nickname(userInfo.getName())
			.profileImageUrl(userInfo.getPicture())
			.role(UserRole.USER_BASIC)
			.build());
	}
}
