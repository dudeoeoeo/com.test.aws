package com.test.aws.springboot.domain.posts;

import javafx.geometry.Pos;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest // 별다른 설정 없이 @SpringBootTest 를 사용할 경우 H2 데이터베이스를 자동 실행
public class PostsRepositoryTest {

    @Autowired
    PostsRepository postsRepository;

    // JUnit에서 단위 테스트가 끝날 때마다 수행되는 메소드를 지정
    // 배포 전 전체 테스트를 수행할 때 테스트간 데이터 침범을 막기 위해 사용
    // 여러 테스트가 동시에 수행되면 테스트용 데이터베이스인 H2에 데이터가 그대로 남아 있어
    // 다음 테스트 실행 시 테스트가 실패할 수 있다.
    @After
    public void cleanup() {
        postsRepository.deleteAll();
    }

    @Test
    public void 게시글저장_불러오기() {
        // given
        String title = "테스트 게시글";
        String content = "테스트 본문";

        // save => 테이블에 id값이 있다면 update, 없으면 insert 쿼리를 날림
        postsRepository.save(Posts.builder()
                                .title(title)
                                .content(content)
                                .author("khy@naver.com")
                                .build());

        // when
        List<Posts> postsList = postsRepository.findAll();

        // then
        Posts posts = postsList.get(0);
        assertThat(posts.getTitle()).isEqualTo(title);
        assertThat(posts.getContent()).isEqualTo(content);
    }
}
