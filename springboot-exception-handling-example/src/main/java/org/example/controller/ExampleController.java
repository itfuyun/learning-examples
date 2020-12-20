package org.example.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author <a href="mailto:itfuyun@gmail.com">Tanxh</a>
 * @since 1.0
 */
@RestController
@RequestMapping("/example")
public class ExampleController {

    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }

    @GetMapping("/exception")
    public String exception() throws Exception {
        throw new Exception("发生错误");
    }
}
