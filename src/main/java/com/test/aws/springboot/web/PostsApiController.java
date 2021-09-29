package com.test.aws.springboot.web;

import com.test.aws.springboot.domain.posts.PostsRepository;
import com.test.aws.springboot.service.posts.PostsService;
import com.test.aws.springboot.web.dto.PostsResponseDto;
import com.test.aws.springboot.web.dto.PostsSaveRequestDto;

import com.test.aws.springboot.web.dto.PostsUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class PostsApiController {

    private final PostsService postsService;

    @PostMapping("/api/v1/posts")
    public Long save(@RequestBody PostsSaveRequestDto requestDto) {
        System.out.println("Post /api/v1/posts called: "+requestDto.toString());
        return postsService.save(requestDto);
    }

    @PutMapping("/api/v1/posts/{id}")
    public Long update(@PathVariable Long id, @RequestBody PostsUpdateRequestDto requestDto) {
        return postsService.update(id, requestDto);
    }
    @GetMapping("/api/v1/posts/{id}")
    public PostsResponseDto findById(@PathVariable Long id) {
        return postsService.findById(id);
    }

    @DeleteMapping("/api/v1/posts/{id}")
    public Long delete(@PathVariable Long id) {
        System.out.println("Delete called id = "+id);
        postsService.delete(id);
        return id;
    }
}
