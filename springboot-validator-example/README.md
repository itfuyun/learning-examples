# 优雅的进行参数校验
>实际项目开发中，对于请求参数校验是必不可少的，现实中很多人校验参数都是写在service中，代码极其不友好

## 普通型
```
@PostMapping("/user/add")
public String add(@RequestBody UserDTO userDTO) {
    if(StringUtils.isEmpty(userDTO.getLoginName())){
        return "用户名不能为空";
    }
    if(StringUtils.isEmpty(userDTO.getPassword())){
        return "密码不能为空";
    }
    if(StringUtils.isEmpty(userDTO.getPhone())){
        return "手机不能为空";
    }
    //业务执行
    return "ok";
}
```
针对每个字段通过if判断，代码量多，而且相对开发人员很不友好

## 优雅型
利用hibernate validator来实现优雅的校验参数

添加依赖，注意springboot从2.3.x开始校验需要单独引入，之前版本不需要添加
```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
</dependency>
```
编写实体类，只需要在参数字段上添加几个注解就可以实现自动校验
```
public class UserDTO {
    /**
     * 登录名
     */
    @NotEmpty(message = "登录名不能为空")
    private String loginName;
    /**
     * 登录密码
     */
    @NotEmpty(message = "登录密码不能为空")
    private String password;
    /**
     * 手机号码
     */
    @NotEmpty(message = "手机号码不能为空")
    private String phone;
    //省略get、set...
}
```
修改下前面的controller，在参数前面加上@Validated注解
```
@PostMapping("/user/add")
public void add(@Validated @RequestBody UserDTO userDTO) {
    userService.insert(userDTO);
}
```
系统自动校验过程中，如果校验不通过将会抛出异常，因此结合另一篇文章：[springboot统一异常处理](./springboot-exception-handling-example) 
让代码更优雅。

添加异常处理
```
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 校验参数异常统一处理
     *
     * @param e
     * @return
     * 没有@RequestBody时候抛出此异常  后端接收为表单提交形式的参数时
     * https://github.com/spring-projects/spring-framework/issues/14790
     */
    @ExceptionHandler(BindException.class)
    public String handleException(BindException e) {
        //获取校验结果
        BindingResult bindingResult = e.getBindingResult();
        StringBuilder stringBuilder = new StringBuilder();
        for (FieldError error : bindingResult.getFieldErrors()) {
            String field = error.getField();
            Object value = error.getRejectedValue();
            String msg = error.getDefaultMessage();
            String message = String.format("错误字段：%s，错误值：%s，原因：%s；", field, value, msg);
            stringBuilder.append(message).append("\r\n");
        }
        return stringBuilder.toString();
    }

    /**
     * 校验参数异常统一处理
     *
     * @param e
     * @return
     * @RequestBody 时候抛出此异常  后端接收为json形式的参数时
     * https://github.com/spring-projects/spring-framework/issues/14790
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public String handleException(MethodArgumentNotValidException e) {
        //获取校验结果
        BindingResult bindingResult = e.getBindingResult();
        StringBuilder stringBuilder = new StringBuilder();
        for (FieldError error : bindingResult.getFieldErrors()) {
            String field = error.getField();
            Object value = error.getRejectedValue();
            String msg = error.getDefaultMessage();
            String message = String.format("错误字段：%s，错误值：%s，原因：%s；", field, value, msg);
            stringBuilder.append(message).append("\r\n");
        }
        return stringBuilder.toString();
    }

    /**
     * 异常处理
     *
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    public Object handleException(Exception e) {
        //可返回自定义信息，此处演示直接返回错误信息
        return e.getMessage();
    }
}
```
此处需要注意，校验不过的时候会抛出不同异常，比如@RequestBody修饰的参数实体抛出MethodArgumentNotValidException异常，
而没有@RequestBody修饰的参数实体抛出BindException
```
@PostMapping("/user/add")
public String add(@Validated @RequestBody UserDTO userDTO) {
    //此处因为有@RequestBody修饰，因此校验不通过的时候抛出MethodArgumentNotValidException异常
    //业务执行
    return "ok";
}
```
```
@PostMapping("/user/add")
public String add(@Validated UserDTO userDTO) {
    //没有有@RequestBody修饰，因此校验不通过的时候抛出BindException异常
    //业务执行
    return "ok";
}
```
## 测试
使用postman调用接口，系统会自动将校验结果返回。再也不需要写一大堆校验逻辑了

## 扩展
实际开发中我们可能遇到一个实体多处使用，但是校验规则不一样。比如插入用户和更新用户字段校验规则就不一样，
插入用户必须要填写密码，但是修改用户可以不需要填写密码，这样该如何处理？

很简单就可以实现，创建规则分组（其实就是两个接口），我暂且命名为Insert和Update，当然你也可以按自己喜好来命名
```
public interface Insert {
}
```
```
public interface Update {
}
```
创建新的实体类，验证注解中填写groups，此处就是注明该条验证在什么分组下有效
```
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
    //省略get、set...
}
```
在controller添加一个测试方法
```
@PostMapping("/user/update")
public String update(@Validated(Update.class) UserGroupDTO userGroupDTO) {
    return "ok";
}
```
在@Validated中指定验证分组即可实现分别校验