package com.test.aws.springboot.domain.posts;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
// Table 과 링크될 클래스
// 기본값으로 클래스의 Camelcase -> Underscore 네이밍으로 테이블 이름 매핑
@Entity // JPA 어노테이션
public class Posts {

    @Id
    // 스프링 부트 2.0 에서는 GenerationType.IDENTITY 옵션을 추가해야만 auto_increment
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Long 타입으로 하면 Mysql 에서는 bigint 가 된다

    @Column(length = 500, nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    private String author;

    @Builder
    public Posts(String title, String content, String author) {
        this.title = title;
        this.content = content;
        this.author = author;
    }
}
