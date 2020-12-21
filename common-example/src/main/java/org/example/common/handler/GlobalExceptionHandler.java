package org.example.common.handler;

import org.example.common.exception.BusinessException;
import org.example.common.response.ResultEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author <a href="mailto:itfuyun@gmail.com">Tanxh</a>
 * @since 1.0
 */
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
