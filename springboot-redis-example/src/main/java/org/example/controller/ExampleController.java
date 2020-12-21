package org.example.controller;

import org.example.common.response.ResultEntity;
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
    public ResultEntity set(String key, String value){
        exampleService.set(key, value);
        return ResultEntity.ok();
    }

    @GetMapping("/get")
    public ResultEntity<String> get(String key){
        String val = exampleService.get(key);
        return ResultEntity.ok(val);
    }

    @PostMapping("/del")
    public ResultEntity set(String key){
        exampleService.del(key);
        return ResultEntity.ok();
    }
}
