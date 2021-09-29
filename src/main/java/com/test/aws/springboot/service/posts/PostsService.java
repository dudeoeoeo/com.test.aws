package com.test.aws.springboot.service.posts;

import com.test.aws.springboot.domain.posts.Posts;
import com.test.aws.springboot.domain.posts.PostsRepository;
import com.test.aws.springboot.web.dto.PostsListResponseDto;
import com.test.aws.springboot.web.dto.PostsResponseDto;
import com.test.aws.springboot.web.dto.PostsSaveRequestDto;

import com.test.aws.springboot.web.dto.PostsUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostsService {

    private final PostsRepository postsRepository;

    @Transactional
    public Long save(PostsSaveRequestDto requestDto) {
        return postsRepository.save(requestDto.toEntity()).getId();
    }

    @Transactional
    public Long update(Long id, PostsUpdateRequestDto requestDto) {
        Posts posts = postsRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id="+ id));
        // update 쿼리를 날리는 부분이 없음
        // JPA의 영속성 컨테이너는 Entity를 영구적으로 저장하는 환경이다 (논리적 개념)
        // JPA의 EntityManager가 활성화된 상태로 트랜잭션 안에서 데이터베이스에서 데이터를 가져오면 이 데이터는
        // 영속성 컨텍스트가 유지된 상태이다.
        // 이 상태에서 해당 데이터의 값을 변경하면 트랜잭션이 끝나는 시점에 해당 테이블에 변경분을 반영
        // 즉, Entity 객체의 값만 변경하면 별도로 Update 쿼리를 날릴 필요가 없다. 이 개념을 dirty checking 이라 부른다.
        posts.update(requestDto.getTitle(), requestDto.getContent());

        return id;
    }
    // readOnly == true -> 트랜잭션 범위는 유지하되, 조회 기능만 남겨두어
    // 조회 속도가 개선되기 때문에 등록, 수정, 삭제 기능이 전혀 없는 서비스 메소드에서 사용
    @Transactional(readOnly = true)
    public List<PostsListResponseDto> findAllDesc() {
        return postsRepository.findAllDesc().stream()
                .map(PostsListResponseDto::new) // == .map(posts -> new PostsListResponseDto(posts))
                .collect(Collectors.toList());
    }

    @Transactional
    public void delete(Long id) {
        Posts posts = postsRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));
        postsRepository.delete(posts);
    }

    public PostsResponseDto findById(Long id) {
        Posts entity = postsRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id="+ id));

        return new PostsResponseDto(entity);
    }
}
