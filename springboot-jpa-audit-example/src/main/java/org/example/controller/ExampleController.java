package org.example.controller;

import org.example.common.response.ResultEntity;
import org.example.entity.UserEntity;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 示例
 *
 * @author <a href="mailto:itfuyun@gmail.com">Tanxh</a>
 * @since 1.0
 */
@RestController
@RequestMapping("/example")
public class ExampleController {

    @Autowired
    UserService userService;

    @PostMapping("/user/add")
    public ResultEntity<UserEntity> add(String username){
        UserEntity userEntity = userService.insert(username);
        return ResultEntity.ok(userEntity);
    }
}
