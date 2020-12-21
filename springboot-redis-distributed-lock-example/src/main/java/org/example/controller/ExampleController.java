package org.example.controller;

import org.example.service.ExampleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 示例控制器
 *
 * @author <a href="mailto:itfuyun@gmail.com">Tanxh</a>
 * @since 1.0
 */
@RestController
@RequestMapping("/lock")
public class ExampleController {
    @Autowired
    ExampleService exampleService;

    @GetMapping
    public String testLock() {
        return exampleService.testLock();
    }
}
