package com.akatsuki.newsum.common.security;

import com.akatsuki.newsum.user.domain.ProviderType;
import com.akatsuki.newsum.user.domain.SocialLogin;
import com.akatsuki.newsum.user.domain.User;
import com.akatsuki.newsum.user.domain.Role;
import com.akatsuki.newsum.user.dto.KakaoUserInfoDto;
import com.akatsuki.newsum.user.repository.SocialLoginRepository;
import com.akatsuki.newsum.user.repository.UserRepository;
import jakarta.servlet.ServletException;
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
                                        Authentication authentication) throws IOException, ServletException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        KakaoUserInfoDto kakaoUser = new KakaoUserInfoDto(oAuth2User.getAttributes());

        String email = kakaoUser.getEmail();
        String nickname = kakaoUser.getNickname();
        String profileImg = kakaoUser.getProfile_image_url(); // 항상 null-safe
        String socialId = kakaoUser.getSocial_id();


        ProviderType providerType = ProviderType.KAKAO;

        User user = userRepository.findByEmail(email).orElseGet(() -> {
            User newUser = User.builder()
                    .email(email)
                    .nickname(nickname)
                    .profile_image_url(profileImg)
                    .socialId(socialId)
                    .createdAt(LocalDateTime.now())
                    .role(Role.USER_BASIC)
                    .build();
            return userRepository.save(newUser);
        });

        // 2) SocialLogin 저장 (중복 방지)
        if (!socialLoginRepository.existsByUserAndProviderType(user, providerType)) {
            SocialLogin sl = SocialLogin.builder()
                    .user(user)
                    .providerId(socialId)
                    .providerType(providerType)
                    .build();
            socialLoginRepository.save(sl);
        }

        try {
            log.info("✔️ 사용자 ID: {}", user.getId());
            log.info("✔️ 사용자 ROLE: {}", user.getRole());

            // 3) 토큰 생성
            String accessToken = tokenProvider.createAccessToken(user.getId(), user.getEmail(), user.getRole());

            // 4) 쿠키 생성 및 설정
            Cookie cookie = new Cookie("access_token", accessToken);
            cookie.setHttpOnly(true);        // JS 접근 차단
            cookie.setSecure(false);         // 개발 환경에서는 false
            cookie.setPath("/");            // 앱 전역에서 전송
            cookie.setMaxAge(60 * 60 * 24);   // 1일(초 단위)
            response.addCookie(cookie);

            // 5) 리다이렉트
            response.sendRedirect("http://localhost:5173/");
        } catch (Exception e) {
            log.error("🔥 로그인 후 처리 중 오류 발생", e);
            response.sendRedirect("/error");
        }

    }
}

