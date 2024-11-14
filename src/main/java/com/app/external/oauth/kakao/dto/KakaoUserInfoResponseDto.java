package com.app.external.oauth.kakao.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;


@Getter @Setter
public class KakaoUserInfoResponseDto {

    private String id;

    @JsonProperty("kakao_account")
    private KakaoAccount kakaoAccount;

    //서버가 외부 API로부터 받은 데이터를 처리
    //JSON 응답(kakaoAccount)의 계층 구조(kakao_account, profile)를 반영하여 각각 내부 클래스로 처리
    @Getter @Setter
    public static class KakaoAccount {
        private String email;
        private Profile profile;

        @Getter @Setter
        public static class Profile {

            private String nickname;

            @JsonProperty("thumbnail_image_url")
            private String thumbnailImageUrl;

        }

    }

}
