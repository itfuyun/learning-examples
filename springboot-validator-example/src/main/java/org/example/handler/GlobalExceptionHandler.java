package org.example.handler;

import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author <a href="mailto:itfuyun@gmail.com">Tanxh</a>
 * @since 1.0
 */
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
