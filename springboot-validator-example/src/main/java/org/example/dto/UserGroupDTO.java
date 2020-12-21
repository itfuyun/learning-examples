package org.example.dto;

import org.example.group.Insert;
import org.example.group.Update;

import javax.validation.constraints.NotEmpty;

/**
 * @author <a href="mailto:itfuyun@gmail.com">Tanxh</a>
 * @since 1.0
 */
public class UserGroupDTO {
    /**
     * 登录名
     */
    @NotEmpty(message = "登录名不能为空", groups = {Insert.class, Update.class})
    private String loginName;
    /**
     * 登录密码
     */
    @NotEmpty(message = "登录密码不能为空", groups = {Insert.class})
    private String password;
    /**
     * 手机号码
     */
    @NotEmpty(message = "手机号码不能为空", groups = {Insert.class, Update.class})
    private String phone;

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

}
