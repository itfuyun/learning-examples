package org.example.controller;

import org.example.dto.UserDTO;
import org.example.dto.UserGroupDTO;
import org.example.group.Update;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author <a href="mailto:itfuyun@gmail.com">Tanxh</a>
 * @since 1.0
 */
@RestController
@RequestMapping("/example")
public class ExampleController {

    /**
     * 验证
     *
     * @param userDTO
     * @return
     */
    @PostMapping("/user/add")
    public String add(@Validated UserDTO userDTO) {
        return "ok";
    }

    /**
     * 分组验证
     *
     * @param userGroupDTO
     * @return
     */
    @PostMapping("/user/update")
    public String update(@Validated(Update.class) UserGroupDTO userGroupDTO) {
        return "ok";
    }
}
