package com.test.aws.springboot.config.auth;

import com.test.aws.springboot.domain.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@RequiredArgsConstructor
// Spring Security 설정들을 활성화시킨다
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomOAuth2UserService customOAuth2UserService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .headers().frameOptions().disable() // h2-console 화면을 사용하기 위해 해당 옵션들을 disable 해야 한다
                .and()
                    // URL별 권한 관리를 설정하는 옵션의 시작점
                    // authorizeRequests 가 선언돼야 antMatchers 옵션 사용 가능
                    .authorizeRequests()
                    // 권한 관리 대상 지정 옵션
                    // URL, HTTP 메소드별로 관리 가능
                    // "/"등 지정된 URL 들은 permitAll() 옵션을 통해 전체 열람 권한
                    // "/api/v1/**" 주소를 가진 API는 USER권한이 있는 사람만 이용 가능
                    .antMatchers("/", "/css/**", "/images/**", "/js/**", "/h2-console/**", "/profile").permitAll()
                    .antMatchers("/api/v1/**").hasRole(Role.USER.name())
                    // 설정된 값들 이외 나머지 URL, authenticated() 를 추가하여 나머지 URL은 인증된 사용자만 허용
                    .anyRequest().authenticated()
                .and()
                    .logout()
                        .logoutSuccessUrl("/")
                .and()
                    // OAuth 2 로그인 기능에 대한 여러 설정의 진입점
                    .oauth2Login()
                        // OAuth 2 로그인 성공 이후 사용자 정보를 가져올 때의 설정들을 담당
                        .userInfoEndpoint()
                            // 소셜 로그인 성공 시 후속 조치를 진행할 UserService 인터페이스의 구현체 등록
                            // 리소스 서버(즉, 소셜 서비스들)에서 사용자 정보를 가져온 상태에서 추가로 진행하고자 하는 기능 명시 가능
                            .userService(customOAuth2UserService);
    }
}
