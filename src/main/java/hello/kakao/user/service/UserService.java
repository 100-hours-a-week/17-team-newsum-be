package hello.kakao.user.service;

import hello.kakao.user.domain.ProviderType;
import hello.kakao.user.domain.SocialLogin;
import hello.kakao.user.domain.User;
import hello.kakao.user.dto.UserCreateDto;
import hello.kakao.user.dto.UserLoginDto;
import hello.kakao.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User create(UserCreateDto userCreateDto) {
        User user = User.builder()
                .email(userCreateDto.getEmail())
                .password(passwordEncoder.encode(userCreateDto.getPassword()))
                .createdAt(LocalDateTime.now())
                .profile_image_url("기본이미지경로또는빈값")
                .build();
        userRepository.save(user);
        return user;
    }


    //검증용 메서드

    public User login(UserLoginDto userLoginDto) {
        Optional<User> optuser = userRepository.findByEmail(userLoginDto.getEmail());
        if (!optuser.isPresent()) {
            throw new IllegalArgumentException("email이 존재하지 않는다");
        }
        User user = optuser.get();
        if (!passwordEncoder.matches(userLoginDto.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("패스워드가 일치하지않습니다");
        }

        return user;
    }

    //DB에서 소셜아이디가져오기
    public User getuserbysocialId(String socialId) {
        //optional값이기 때문에 예외처리가 강제적이다
     User user = userRepository.findBySocialId(socialId).orElse(null);
     return user;
    }

    public User createOauth(String email, String socialId, ProviderType providerType) {
        // 1. 유저 먼저 생성
        User user = User.builder()
                .email(email)
                .createdAt(LocalDateTime.now())
                .profile_image_url("기본이미지경로또는빈값")
                .build();

        // 2. 소셜로그인 객체 생성
        SocialLogin socialLogin = SocialLogin.builder()
                .user(user)
                .providerId(socialId)
                .providerType(providerType)
                .build();
        user.getSocialLogins().add(socialLogin);
        userRepository.save(user);
        return user;
    }

}
