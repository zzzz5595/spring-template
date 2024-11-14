package com.app.api.login.service;

import com.app.api.login.dto.OauthLoginDto;
import com.app.domain.member.constant.MemberType;
import com.app.domain.member.constant.Role;
import com.app.domain.member.entity.Member;
import com.app.domain.member.service.MemberService;
import com.app.external.oauth.model.OAuthAttributes;
import com.app.external.oauth.service.SocialLoginApiService;
import com.app.external.oauth.service.SocialLoginApiServiceFactory;
import com.app.global.jwt.dto.JwtTokenDto;
import com.app.global.jwt.service.TokenManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class OauthLoginService {

    private final MemberService memberService;
    private final TokenManager tokenManager;


    public OauthLoginDto.Response oauthLogin(String accessToken, MemberType memberType) {
        // memberType이 KAKAO, GOOGLE, NAVER 등 어떤 로그인 서비스인지에 따라 SocialLoginApiServiceFactory를 통해 해당 소셜 로그인 API 서비스를 선택.
        // 예: KAKAO일 경우 KakaoLoginApiService가 선택됨
        SocialLoginApiService socialLoginApiService = SocialLoginApiServiceFactory.getSocialLoginApiService(memberType);

        //소셜로그인 타입에 따른 유저정보 조회
        OAuthAttributes userInfo = socialLoginApiService.getUserInfo(accessToken);
        log.info("userInfo : {}",  userInfo);

        Optional<Member> optionalMember = memberService.findMemberByEmail(userInfo.getEmail());

        JwtTokenDto jwtTokenDto;
        if(optionalMember.isEmpty()) { // 신규 회원 가입
            Member oauthMember = userInfo.toMemberEntity(memberType, Role.ADMIN);
            oauthMember = memberService.registerMember(oauthMember);

            // 토큰 생성
            jwtTokenDto = tokenManager.createJwtTokenDto(oauthMember.getMemberId(), oauthMember.getRole());

            //추후 DB에 리프레쉬 토큰 및 토큰 시간 업데이트필요(현재는 JPA로 자동저장)
            //리프레쉬토 토큰만 DB에 저장하는 이유는 access token이 데이터베이스에 있는 토큰과 일치하는지를 보면 API 요청이 올 때마다 데이터베이스를 조회해야하기 때문에 성능적으로 좋지않기 때문
            //사용자가 http request시, 만료된 access Token과 아직 살아있는 refresh token을 보내면, 서버 입장에서는 database에 refresh token이 있는지 확인만 되면
            //access token 생성함.
            oauthMember.updateRefreshToken(jwtTokenDto);

        } else { // 기존 회원일 경우
            Member oauthMember = optionalMember.get();

            // 토큰 생성
            jwtTokenDto = tokenManager.createJwtTokenDto(oauthMember.getMemberId(), oauthMember.getRole());
            oauthMember.updateRefreshToken(jwtTokenDto);
        }


        return OauthLoginDto.Response.of(jwtTokenDto);
    }

}
