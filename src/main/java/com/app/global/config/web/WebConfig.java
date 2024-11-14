package com.app.global.config.web;

import com.app.global.interceptor.AdminAuthorizationInterceptor;
import com.app.global.interceptor.AuthenticationInterceptor;
import com.app.global.resolver.memberinfo.MemberInfoArgumentResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
//WebConfig: 스프링이 내부적으로 관리하는 기능들(예: 인터셉터, 정적 리소스 경로, CORS 설정).
public class WebConfig implements WebMvcConfigurer {

    private final AuthenticationInterceptor authenticationInterceptor;
    private final MemberInfoArgumentResolver memberInfoArgumentResolver;
    private final AdminAuthorizationInterceptor adminAuthorizationInterceptor;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
       // "/api/**"로 시작하는 모든 경로에 대해 CORS 설정을 추가
       registry.addMapping("/api/**")
               // 이 API 경로에 대해 요청을 허용할 출처(origin)를 모두 허용하도록 설정
               .allowedOrigins("*")
               // 허용할 HTTP 메서드들을 지정 (GET, POST, PUT, PATCH, DELETE, OPTIONS)
               .allowedMethods(
                       HttpMethod.GET.name(),
                       HttpMethod.POST.name(),
                       HttpMethod.PUT.name(),
                       HttpMethod.PATCH.name(),
                       HttpMethod.DELETE.name(),
                       HttpMethod.OPTIONS.name()
               )
               .maxAge(3600);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //인증 인터셉터
        registry.addInterceptor(authenticationInterceptor)
                .order(1)
                .addPathPatterns("/api/**")
                .excludePathPatterns("/api/oauth/login",
                        "/api/access-token/issue",
                        "/api/logout",
                        "/api/health");
        
        //인가 인터셉터
        registry.addInterceptor(adminAuthorizationInterceptor)
                .order(2)
                .addPathPatterns("/api/admin/**");

    }

    /**
     * @MemberInfo 어노테이션이 붙은 MemberInfoDto 타입의 파라미터를 처리할 때 memberInfoArgumentResolver를 사용.
     * 모든 컨트롤러에서 @MemberInfo 어노테이션이 붙은 파라미터에 자동으로 사용자 정보를 주입
     * resolvers: Spring에서 컨트롤러 메소드의 파라미터에 값을 자동으로 넣어주는 도구들의 목록
     * @param resolvers
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(memberInfoArgumentResolver);
    }


}
