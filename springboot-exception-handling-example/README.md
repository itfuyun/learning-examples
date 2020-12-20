# 统一异常处理
> 在后端发生异常或者是请求出错的时候，Spring默认的异常非常不友好，往往不能满足自己的需求

本文介绍在SpringBoot下如何进行统一异常处理，方式有很多种，这里主要介绍两个很常用的
## 常用方式
- 使用@ControllerAdvice+@ExceptionHandler注解
- 通过实现ErrorController
### 1.使用@ControllerAdvice和@ExceptionHandler注解
缺点：只能处理控制器抛出的异常，比如404错误无法捕获

优点：针对不同异常进行不同逻辑处理
### 2.通过实现ErrorController
缺点：针对不同异常处理比较麻烦，自由度不高

优点：可以处理所有的异常，包括未进入控制器的错误，比如404等错误
## 总结
两种方式都具有一定的优缺点，因此我们可以把两者进行结合使用

第一种：

创建GlobalExceptionHandler类
```
@RestControllerAdvice
public class GlobalExceptionHandler {

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
第二种：

创建ErrorControllerHandler类
```
@RestController
public class ErrorControllerHandler implements ErrorController {
    /**
     * 错误路径，默认发生错误会跳转到/error
     */
    private static final String ERROR_PATH = "/error";

    private ErrorAttributes errorAttributes;

    @Autowired
    public ErrorControllerHandler(ErrorAttributes errorAttributes) {
        this.errorAttributes = errorAttributes;
    }

    /**
     * 错误处理
     *
     * @param request
     * @param ex
     * @param req
     * @return
     */
    @RequestMapping(value = ERROR_PATH)
    @ExceptionHandler(value = {Exception.class})
    @ResponseBody
    public Object errorApiHandler(HttpServletRequest request, final Exception ex, final WebRequest req) {
        RequestAttributes requestAttributes = new ServletRequestAttributes(request);
        //获取到默认错误信息
        Map<String, Object> attr = this.errorAttributes.getErrorAttributes(req, ErrorAttributeOptions.defaults());
        int status = getStatus(request);
        switch (status) {
            case 401:
                return "未认证";
            case 404:
                return "请求路径不存在";
            case 403:
                return "无权限";
            default:
                return "系统繁忙";
        }

    }

    /**
     * 获取错误码
     *
     * @param request
     * @return
     */
    private int getStatus(HttpServletRequest request) {
        Integer status = (Integer) request.getAttribute("javax.servlet.error.status_code");
        if (status != null) {
            return status;
        }
        return 500;
    }

    @Override
    public String getErrorPath() {
        return ERROR_PATH;
    }
}
```
## 测试
创建测试用的ExampleController类
```
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
```
使用postman工具分别访问端点/example/hello和/example/exception。可以发现
异常被GlobalExceptionHandler统一处理了

再访问一个不存在的端点/example/test。此时会进入ErrorControllerHandler中处理，至此统一异常已经完成。

## 结语
上述列子只是简单的做了一些示例，实际情况我们会封装的更完善，比如统一返回一个实体，当作返回值。再比如在
GlobalExceptionHandler中，我们会针对不同异常分别写一些处理访问，同时结合自定义异常达到更好的效果