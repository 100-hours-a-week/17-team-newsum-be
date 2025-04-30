package com.akatsuki.newsum.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KakaoUserInfoDto {
    private String email;
    private String nickname;
    private String profile_image_url;
    private String social_id;

    public KakaoUserInfoDto(Map<String, Object> attributes) {
        this.email = (String) ((Map<String, Object>) attributes.get("kakao_account")).get("email");
        this.nickname = (String) ((Map<String, Object>) attributes.get("properties")).get("nickname");
        String originalProfileImg = (String) ((Map<String, Object>) attributes.get("properties")).get("profile_image");
        this.profile_image_url = (originalProfileImg == null || originalProfileImg.isEmpty())
                ? "http://localhost:8080/images/default-profile.png"
                : originalProfileImg;
        this.social_id = String.valueOf(attributes.get("id"));

    }
}

