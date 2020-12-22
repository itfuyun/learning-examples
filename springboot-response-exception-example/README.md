# 统一返回结果和统一异常处理
> 在后端发生异常或者是请求出错的时候，Spring默认的异常非常不友好，往往不能满足自己的需求

## 统一返回结果封装

在处理异常之前，我们先来简单封装下统一返回结果

创建错误代码枚举
```
public enum ErrorCodeEnum {

    /**
     * 成功
     */
    SUCCESS(HttpStatus.OK, ErrorCodeEnum.SUCCESS_CODE, "Success"),
    /**
     * 业务异常
     */
    BUSINESS_ERROR(HttpStatus.BAD_REQUEST, ErrorCodeEnum.BUSINESS_ERROR_CODE,"Business Error"),
    /**
     * 请求参数错误
     */
    BAD_REQUEST(HttpStatus.BAD_REQUEST, ErrorCodeEnum.BAD_REQUEST_CODE, "Bad Request"),
    /**
     * 未登陆
     */
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, ErrorCodeEnum.UNAUTHORIZED_CODE, "Unauthorized"),
    /**
     * 无权限
     */
    FORBIDDEN(HttpStatus.FORBIDDEN, ErrorCodeEnum.FORBIDDEN_CODE, "Forbidden"),
    /**
     * 不存在
     */
    NOT_FOUND(HttpStatus.NOT_FOUND, ErrorCodeEnum.NOT_FOUND_CODE, "Not Found"),
    /**
     * 请求方式错误
     */
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, ErrorCodeEnum.METHOD_NOT_ALLOWED_CODE, "Method Not Allowed"),
    /**
     * 系统内部错误
     */
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCodeEnum.INTERNAL_SERVER_ERROR_CODE, "Internal Server Error");

    ErrorCodeEnum(HttpStatus httpStatus, int code, String msg) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.msg = msg;
    }

    private int code;
    private String msg;
    private HttpStatus httpStatus;

    public static final int SUCCESS_CODE = 0;
    public static final int BUSINESS_ERROR_CODE = 10000;
    public static final int BAD_REQUEST_CODE = 400;
    public static final int UNAUTHORIZED_CODE = 401;
    public static final int FORBIDDEN_CODE = 403;
    public static final int NOT_FOUND_CODE = 404;
    public static final int METHOD_NOT_ALLOWED_CODE = 405;
    public static final int INTERNAL_SERVER_ERROR_CODE = 500;

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
```
创建响应结果实体
```
public class ResultEntity<T> {
    private Integer code;
    private String msg;
    private T data;

    private ResultEntity() {
    }

    private ResultEntity(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    /**
     * 成功
     *
     * @param <T>
     * @return
     */
    public static <T> ResultEntity<T> ok() {
        return new ResultEntity<T>(ErrorCodeEnum.SUCCESS.getCode(), ErrorCodeEnum.SUCCESS.getMsg(), null);
    }

    /**
     * 成功
     *
     * @param data
     * @param <T>
     * @return
     */
    public static <T> ResultEntity<T> ok(T data) {
        return new ResultEntity<T>(ErrorCodeEnum.SUCCESS.getCode(), ErrorCodeEnum.SUCCESS.getMsg(), data);
    }

    /**
     * 成功
     *
     * @param msg
     * @param data
     * @param <T>
     * @return
     */
    public static <T> ResultEntity<T> ok(String msg, T data) {
        return new ResultEntity<T>(ErrorCodeEnum.SUCCESS.getCode(), msg, data);
    }

    /**
     * 成功
     *
     * @param code
     * @param msg
     * @param data
     * @param <T>
     * @return
     */
    public static <T> ResultEntity<T> ok(Integer code, String msg, T data) {
        return new ResultEntity<T>(code, msg, data);
    }

    /**
     * 失败
     *
     * @param msg
     * @param <T>
     * @return
     */
    public static <T> ResultEntity<T> failed(String msg) {
        return new ResultEntity<T>(ErrorCodeEnum.INTERNAL_SERVER_ERROR.getCode(), msg, null);
    }

    /**
     * 失败
     *
     * @param code
     * @param msg
     * @param <T>
     * @return
     */
    public static <T> ResultEntity<T> failed(Integer code, String msg) {
        return new ResultEntity<T>(code, msg, null);
    }

    /**
     * 失败
     *
     * @param msg
     * @param data
     * @param <T>
     * @return
     */
    public static <T> ResultEntity<T> failed(String msg, T data) {
        return new ResultEntity<T>(ErrorCodeEnum.SUCCESS.getCode(), msg, data);
    }

    /**
     * 失败
     *
     * @param code
     * @param msg
     * @param data
     * @param <T>
     * @return
     */
    public static <T> ResultEntity<T> failed(Integer code, String msg, T data) {
        return new ResultEntity<T>(code, msg, data);
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
```
创建一个自定义业务异常类
```
public class BusinessException extends Exception {
    private Integer code;
    private String msg;

    public BusinessException(String msg) {
        super(msg);
        this.code = ErrorCodeEnum.BUSINESS_ERROR.getCode();
        this.msg = msg;
    }

    public BusinessException(Integer code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
```
简单封装就完成了

## 统一异常处理

本文介绍在SpringBoot下如何进行统一异常处理，方式有很多种，这里主要介绍两个很常用的
- 使用@ControllerAdvice+@ExceptionHandler注解
- 通过实现ErrorController

### 1.使用@ControllerAdvice和@ExceptionHandler注解

缺点：只能处理控制器抛出的异常，比如404错误无法捕获

优点：针对不同异常进行不同逻辑处理

### 2.通过实现ErrorController

缺点：针对不同异常处理比较麻烦，自由度不高

优点：可以处理所有的异常，包括未进入控制器的错误，比如404等错误

## 具体实现

两种方式都具有一定的优缺点，因此我们可以把两者进行结合使用

第一种：

创建GlobalExceptionHandler类
```
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 业务处理
     *
     * @param e
     * @return
     */
    @ExceptionHandler(BusinessException.class)
    public Object handleException(BusinessException e) {
        //返回错误信息
        return ResultEntity.failed(e.getCode(),e.getMsg());
    }
    
    /**
     * 其他异常处理
     *
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    public Object handleException(Exception e) {
        //返回错误信息
        return ResultEntity.failed(e.getMessage());
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
    public Object errorApiHandler(HttpServletRequest request, final Exception ex, final WebRequest req) {
        RequestAttributes requestAttributes = new ServletRequestAttributes(request);
        //获取到默认错误信息
        Map<String, Object> attr = this.errorAttributes.getErrorAttributes(req, ErrorAttributeOptions.defaults());
        int status = getStatus(request);
        ErrorCodeEnum errorCodeEnum;
        switch (status) {
            case 401:
                errorCodeEnum = ErrorCodeEnum.UNAUTHORIZED;
                break;
            case 404:
                errorCodeEnum = ErrorCodeEnum.NOT_FOUND;
                break;
            case 403:
                errorCodeEnum = ErrorCodeEnum.FORBIDDEN;
                break;
            default:
                errorCodeEnum = ErrorCodeEnum.INTERNAL_SERVER_ERROR;
        }
        return ResultEntity.failed(errorCodeEnum.getCode(),errorCodeEnum.getMsg());
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
    public ResultEntity<String> hello() {
        return ResultEntity.ok("hello");
    }

    @GetMapping("/exception")
    public ResultEntity exception() throws Exception {
        throw new BusinessException("发生业务异常");
    }
}
```
使用postman工具分别访问端点/example/hello和/example/exception。可以发现异常被GlobalExceptionHandler统一处理了

再访问一个不存在的端点/example/test。此时会进入ErrorControllerHandler中处理，至此统一异常已经完成。

## 结语
总之：统一异常+统一返回结果+自定义异常可以极大的提升代码友好度，当你计划搭建一个项目基础框架时，这些功能可以说是必不可少的，实际项目开发中我们往往会将这些统一封装提取出来做成一个common模块或项目，然后通过maven引用公共模块