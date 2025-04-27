package hello.kakao.user.service;


import hello.kakao.user.dto.AccessTokenDto;
import hello.kakao.user.dto.GoogleProfileDto;
import org.springframework.stereotype.Service;

@Service
public class GoogleService {
    public AccessTokenDto getAccessToken() {
        //구글한테 인가코드, 클라id, 클라secret, 리다url, grant_type 넘길거다
    }

    public GoogleProfileDto getGoogleProfile(String token) {

    }
}
