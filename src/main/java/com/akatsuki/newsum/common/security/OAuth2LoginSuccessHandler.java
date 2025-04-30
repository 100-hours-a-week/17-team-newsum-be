package com.akatsuki.newsum.common.security;

import com.akatsuki.newsum.user.domain.ProviderType;
import com.akatsuki.newsum.user.domain.Role;
import com.akatsuki.newsum.user.domain.SocialLogin;
import com.akatsuki.newsum.user.domain.User;
import com.akatsuki.newsum.user.dto.KakaoUserInfoDto;
import com.akatsuki.newsum.user.repository.SocialLoginRepository;
import com.akatsuki.newsum.user.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final SocialLoginRepository socialLoginRepository;
    private final TokenProvider tokenProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        Map<String, Object> attributes = oAuth2User.getAttributes();


        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> properties = (Map<String, Object>) attributes.get("properties");

        String email = (String) kakaoAccount.get("email");
        String nickname = (String) properties.get("nickname");
        String originalProfileImg = (String) properties.get("profile_image");
        String profileImg = (originalProfileImg == null || originalProfileImg.isEmpty())
                ? "http://localhost:8080/images/default-profile.png"
                : originalProfileImg;
        String socialId = String.valueOf(attributes.get("id"));

        ProviderType providerType = ProviderType.KAKAO;

        // 👉 정제된 값만 넘기기
        KakaoUserInfoDto kakaoUser = new KakaoUserInfoDto(email, nickname, profileImg, socialId);

        User user = userRepository.findByEmail(email)
                .orElseGet(() -> {
                    User newUser = User.builder()
                            .email(email)
                            .nickname(nickname)
                            .profileImageUrl(profileImg)
                            .socialId(socialId)
                            .createdAt(LocalDateTime.now())
                            .role(Role.USER_BASIC)
                            .build();
                    return userRepository.save(newUser);
                });

        checkDuplicateSocialProviderAndSave(user, providerType, socialId);

        try {
            log.info("✔️ 사용자 ID: {}", user.getId());
            log.info("✔️ 사용자 ROLE: {}", user.getRole());

            String accessToken = tokenProvider.createAccessToken(user.getId(), user.getEmail(), user.getRole());
            String refreshToken = tokenProvider.createRefreshToken();

            Cookie accessCookie = new Cookie("access_token", accessToken);
            accessCookie.setHttpOnly(true);
            accessCookie.setSecure(false);
            accessCookie.setPath("/");
            accessCookie.setMaxAge(60 * 60 * 24);

            Cookie refreshCookie = new Cookie("refresh_token", refreshToken);
            refreshCookie.setHttpOnly(true);
            refreshCookie.setSecure(false);
            refreshCookie.setPath("/");
            refreshCookie.setMaxAge(60 * 60 * 24 * 7);

            response.addCookie(accessCookie);
            response.addCookie(refreshCookie);

            // refreshTokenService.save(user.getId(), refreshToken);

            response.sendRedirect("http://localhost:5173/");
        } catch (Exception e) {
            log.error("🔥 로그인 후 처리 중 오류 발생", e);
            response.sendRedirect("/error");
        }
    }

    private void checkDuplicateSocialProviderAndSave(User user, ProviderType providerType, String socialId) {
        if (!socialLoginRepository.existsByUserAndProviderType(user, providerType)) {
            SocialLogin socialLogin = SocialLogin.builder()
                    .user(user)
                    .providerId(socialId)
                    .providerType(providerType)
                    .build();
            socialLoginRepository.save(socialLogin);
        }
    }
}
