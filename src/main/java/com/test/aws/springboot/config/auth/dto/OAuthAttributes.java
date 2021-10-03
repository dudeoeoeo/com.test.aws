package com.test.aws.springboot.config.auth.dto;

import com.test.aws.springboot.domain.user.Role;
import com.test.aws.springboot.domain.user.User;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
public class OAuthAttributes {

    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private String name;
    private String email;
    private String picture;

    @Builder
    public OAuthAttributes(Map<String, Object> attributes, String nameAttributeKey, String name, String email, String picture) {
        System.out.println("OAuthAttributes 호출");
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.name = name;
        this.email = email;
        this.picture = picture;
    }

    // OAuth2User 에서 반환하는 사용자 정보는 Map이기 때문에 값 하나하나를 변환해야 한다.
    public static OAuthAttributes of(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {
        System.out.println("OAuthAttributes of 호출");
        if("naver".equals(registrationId))
            return ofNaver("id", attributes);

        return ofGoogle(userNameAttributeName, attributes);
    }

    public static OAuthAttributes ofNaver(String userNameAttributeName, Map<String, Object> attributes) {
        System.out.println("OAuthAttributes ofNaver 호출");
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");
        System.out.println("response: "+response.toString());
        System.out.println("name: "+(String) response.get("name"));
        System.out.println("email: "+(String) response.get("email"));
        System.out.println("profile_image: "+(String) response.get("profile_image"));
        return OAuthAttributes.builder()
                .name((String) response.get("name"))
                .email((String) response.get("email"))
                .picture((String) response.get("profile_image"))
                .attributes(response)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    public static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
        System.out.println("OAuthAttributes ofGoogle 호출");
        System.out.println("ofGoogle attributes: "+attributes.toString());
        return OAuthAttributes.builder()
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .picture((String) attributes.get("picture"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    // User Entity를 생성
    // 처음 가입할 때 기본 권한을 GUEST로 준다
    public User toEntity() {
        System.out.println("User toEntity 호출");
        System.out.println("name: "+name+", email: "+email+", picture: "+picture);
        return User.builder()
                .name(name)
                .email(email)
                .picture(picture)
                .role(Role.GUEST)
                .build();
    }
}
