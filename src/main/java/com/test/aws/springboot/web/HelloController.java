package com.test.aws.springboot.web;

import com.test.aws.springboot.web.dto.HelloResponseDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

// JSON 을 반환하는 컨트롤러로 만들어 준다.
// 전에는 @ResponseBody 를 각 메소드마다 선언했던 것을 한번에 사용할 수 있게 한다.
@RestController
public class HelloController {

    // @RequestMapping(method = RequestMethod.GET)
    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }

    @GetMapping("/hello/dto")
    public HelloResponseDto helloDto(@RequestParam("name") String name, @RequestParam("amount") int amount) {
        return new HelloResponseDto(name, amount);
    }
}
