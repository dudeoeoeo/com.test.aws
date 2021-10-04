package com.test.aws.springboot.web;

import com.test.aws.springboot.config.auth.LoginUser;
import com.test.aws.springboot.config.auth.dto.SessionUser;
import com.test.aws.springboot.domain.user.Role;
import com.test.aws.springboot.domain.user.User;
import com.test.aws.springboot.service.posts.PostsService;
import com.test.aws.springboot.web.dto.PostsResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@Controller
public class IndexController {

    private final PostsService postsService;
    private final HttpSession httpSession;

    // mustache 스타터가 컨트롤러에서 문자열을 반환할 때 경로와 확장자 지정
    // 기본 경로 src/main/resources/templates
    @GetMapping("/")
    public String index(Model model, @LoginUser SessionUser user) {
        model.addAttribute("posts", postsService.findAllDesc());

        System.out.println("index called: "+user);
        if(user != null) {
            System.out.println("user: "+user.getEmail()+", "+user.getName());
            model.addAttribute("userName", user.getName());
        }

        return "index";
    }

    @GetMapping("/posts/update/{id}")
    public String postsUpdate(@PathVariable Long id, Model model) {
        PostsResponseDto postsResponseDto = postsService.findById(id);
        model.addAttribute("post", postsResponseDto);

        return "posts-update";
    }

    @GetMapping("/posts/save")
    public String postsSave() {
        return "posts-save";
    }
}
