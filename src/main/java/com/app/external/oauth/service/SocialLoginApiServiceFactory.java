package com.app.external.oauth.service;

import com.app.domain.member.constant.MemberType;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class SocialLoginApiServiceFactory {

    /**
     * SocialLoginApiService 인터페이스를 구현한 카카오, 구글, 페이스북 로그인 서비스 클래스들 주입받기위함.
     *  - 그럼 SocialLoginApiService 주입받은 클래스들의 이름이 key, 해당 클래스가 구현체가 되므로 value가 됨.
     *  -
     * 주입받지 않는경우(아래처럼 직접 선언해야하는 문제가생김)
     * Map<String, SocialLoginApiService> mockServices = new HashMap<>();
     * mockServices.put("kakaoLoginApiServiceImpl", new MockKakaoLoginApiService());
     * mockServices.put("googleLoginApiServiceImpl", new MockGoogleLoginApiService());
     */
    private static Map<String, SocialLoginApiService> socialLoginApiServices;

    public SocialLoginApiServiceFactory(Map<String, SocialLoginApiService> socialLoginApiServices) {
        this.socialLoginApiServices = socialLoginApiServices;
    }

    public static SocialLoginApiService getSocialLoginApiService(MemberType memberType) {
        String socialLoginApiServiceBeanName = "";

        if(MemberType.KAKAO.equals(memberType)) {
            socialLoginApiServiceBeanName = "kakaoLoginApiServiceImpl";
        }
        return socialLoginApiServices.get(socialLoginApiServiceBeanName);
    }

}
