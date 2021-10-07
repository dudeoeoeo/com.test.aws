package com.test.aws.springboot.web;

import com.test.aws.springboot.config.auth.SecurityConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// 테스트를 진행할 때 JUnit에 내장된 실행자 외에 다른 실행자를 실행
// 여기서는 SpringRunner 라는 스프링 실행자 사용
// 스프링 부트 테스트와 JUnit 사이에 연결자 역할
@RunWith(SpringRunner.class)

// 여러 스프링 테스트 어노테이션 중, Web(Spring MVC)에 집중할 수 있는 어노테이션
// 선언할 경우 @Controller, @ControllerAdvice 사용 가능
// @Service, @Component, @Repository 사용 불가
// WebMvcTest는 @Service, @Component, @Repository 이 어노테이션들이 스캔 대상이 아니다.
// @WebMvcTest(controllers = HelloController.class)
@WebMvcTest(controllers = HelloController.class,
        excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
                classes = SecurityConfig.class)
        }
)
public class HelloControllerTest {

    // 웹 API 테스트 시 사용
    // 스프링 MVC 테스트의 시작점
    // 이 클래스를 사용하여 HTTP GET, POST 등에 대한 API 테스트 가능
    @Autowired
    private MockMvc mockMvc;

    @WithMockUser(roles = "USER")
    @Test
    public void hello() throws Exception {
        String hello = "hello";

        // MockMvc를 사용하여 /hello 주소로 HTTP GET 요청
        // 체이닝이 지원되어 아래와 같이 어려 검증 기능 이어서 선언 가능
        mockMvc.perform(get("/hello"))
                // mockMvc.perform의 결과 검증
                // HTTP Header의 Status 검증
                // 200, 404, 500 등의 상태 검증, 여기선 OK == 200 인지 검증
                .andExpect(status().isOk())
                // mockMvc.perform의 결과 검증
                // 응답 본문의 내용 검증
                // Controller에서 "hello"를 리턴하기 때문에 이 값이 맞는지 검증
                .andExpect(content().string(hello));
    }
    @WithMockUser(roles = "USER")
    @Test
    public void helloDto() throws Exception {
        String name = "hello";
        int amount = 1000;
        
        mockMvc.perform(
                get("/hello/dto")
                            .param("name", name) // API 테스트 시 사용될 요청 마라미터 설정, 값은 String만 허용
                            .param("amount", String.valueOf(amount))) // 숫자, 날짜 등의 데이터도 문자열로 변경해야 함
                .andExpect(status().isOk())
                // JSON 응답값을 필드별로 검증할 수 있는 메소드
                // $를 기준으로 필드명을 명시합니다.
                // 여기서는 name과 amount를 검증하니 $.name, $.amount 로 검증
                .andExpect(jsonPath("$.name", is(name)))
                .andExpect(jsonPath("$.amount", is(amount)));
    }
}
