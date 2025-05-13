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
import com.akatsuki.newsum.domain.user.dto.GoogleUserInfo;
import com.akatsuki.newsum.domain.user.entity.Provider;
import com.akatsuki.newsum.domain.user.entity.SocialLogin;
import com.akatsuki.newsum.domain.user.entity.Status;
import com.akatsuki.newsum.domain.user.entity.User;
import com.akatsuki.newsum.domain.user.entity.UserRole;
import com.akatsuki.newsum.domain.user.generator.NicknameGenerator;
import com.akatsuki.newsum.domain.user.repository.SocialLoginRepository;
import com.akatsuki.newsum.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GoogleOAuthService {

	private final UserRepository userRepository;
	private final SocialLoginRepository socialLoginRepository;
	private final TokenProvider tokenProvider;
	private final NicknameGenerator nicknameGenerator;

	@Value("${GOOGLE_CLIENT_ID}")
	private String clientId;

	@Value("${GOOGLE_CLIENT_SECRET}")
	private String clientSecret;

	@Value("${GOOGLE_REDIRECT_URI}")
	private String redirectUri;

	@Value("${user.default-profile-image-url}")
	private String defaultProfileImageUrl;

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
			String providerId = userInfo.getId();

			SocialLogin socialLogin = new SocialLogin(user, providerId, Provider.GOOGLE);
			socialLoginRepository.save(socialLogin);
		}

		// 5. JWT 발급
		String accessJwt = tokenProvider.createAccessToken(user.getId(), user.getEmail(), user.getRole());
		String refreshJwt = tokenProvider.createRefreshToken();

		return new TokenResponse(accessJwt, refreshJwt, userInfo);
	}

	private String getAccessToken(String code) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
		formData.add("code", code);
		formData.add("client_id", clientId);
		formData.add("client_secret", clientSecret);
		formData.add("redirect_uri", redirectUri);
		formData.add("grant_type", "authorization_code");

		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(formData, headers);

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
			(String)body.get("id")
		);
	}

	private User saveNewUser(GoogleUserInfo userInfo) {
		String nickname = nicknameGenerator.generate();
		return userRepository.save(User.builder()
			.email(userInfo.getEmail())
			.nickname(nickname)
			.profileImageUrl(defaultProfileImageUrl)
			.role(UserRole.USER_BASIC)
			.status(Status.ACTIVATE)
			.build());
	}
}
