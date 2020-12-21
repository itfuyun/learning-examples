package org.example.controller;

import org.example.common.exception.BusinessException;
import org.example.common.response.ResultEntity;
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
    public ResultEntity<String> hello() {
        return ResultEntity.ok("hello");
    }

    @GetMapping("/exception")
    public ResultEntity exception() throws Exception {
        throw new BusinessException("发生业务异常");
    }
}
