package com.test.aws.springboot.config.auth.dto;

import com.test.aws.springboot.domain.user.User;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class SessionUser implements Serializable {
    private String name;
    private String email;
    private String picture;

    public SessionUser(User user) {
        System.out.println("SessionUser 생성자 만들어짐");
        this.name = user.getName();
        this.email = user.getEmail();
        this.picture = user.getPicture();
    }
}
