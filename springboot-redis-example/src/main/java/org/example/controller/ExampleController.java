package org.example.controller;

import org.example.service.ExampleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 示例控制器
 *
 * @author <a href="mailto:itfuyun@gmail.com">Tanxh</a>
 * @since 1.0
 */
@RestController
@RequestMapping("/redis")
public class ExampleController {

    @Autowired
    ExampleService exampleService;

    @PostMapping("/set")
    public String set(String key,String value){
        exampleService.set(key, value);
        return "ok";
    }

    @GetMapping("/get")
    public String get(String key){
        return exampleService.get(key);
    }

    @PostMapping("/del")
    public String set(String key){
        exampleService.del(key);
        return "ok";
    }
}
