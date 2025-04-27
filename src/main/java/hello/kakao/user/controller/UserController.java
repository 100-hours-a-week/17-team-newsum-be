package hello.kakao.user.controller;


import hello.kakao.common.auth.JwtTokenProvider;
import hello.kakao.user.domain.ProviderType;
import hello.kakao.user.domain.User;
import hello.kakao.user.dto.*;
import hello.kakao.user.service.GoogleService;
import hello.kakao.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final GoogleService googleService;

    public UserController(UserService userService, JwtTokenProvider jwtTokenProvider, GoogleService googleService) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.googleService = googleService;
    }
//회원가입은 안하지만 넣어봄
    @PostMapping("/create")
    public ResponseEntity<?> userCreate(@RequestBody UserCreateDto userCreateDto) {
        User user = userService.create(userCreateDto);
        return new ResponseEntity<>(user.getId(), HttpStatus.CREATED);
    }

    @PostMapping("/dologin")
    //email password 검증
    public ResponseEntity<?> doLogin(@RequestBody UserLoginDto userLoginDto) {
        User user = userService.login(userLoginDto);

        //  일치할 경우 jwt 토큰 생성한다
        String jwtToken = jwtTokenProvider.createToken(user.getEmail(), user.getRole().toString());

        Map<String, Object> loginInfo = new HashMap<>();
        loginInfo.put("id", user.getId());
        loginInfo.put("token", jwtToken);
        return new ResponseEntity<>(jwtToken, HttpStatus.OK);
    }

    @PostMapping("/google/dologin")
    public ResponseEntity<?> googleLogin(@RequestBody RedirectDto redirectDto) {
        //구글에서 토큰발급
        AccessTokenDto accessToken = googleService.getAccessToken();
        //사용자정보 받기
        GoogleProfileDto googleProfileDto = googleService.getGoogleProfile(accessToken.getAccess_Token());

        //회원이 아니면 구글 회원가입
        User originUser = userService.getuserbysocialId(googleProfileDto.getSub());
        if(originUser == null) {
            originUser  = userService.createOauth(googleProfileDto.getEmail(), googleProfileDto.getSub(), ProviderType.GOOGLE);
        }
        //회원가입돼 있는 회원이면 토큰발급한다.
        String jwtToken = jwtTokenProvider.createToken(originUser.getEmail(), originUser.getRole().toString());

        Map<String, Object> loginInfo = new HashMap<>();
        loginInfo.put("id", originUser.getId());
        loginInfo.put("token", jwtToken);
        return new ResponseEntity<>(jwtToken, HttpStatus.OK);

    }
}
