package org.example.common.exception;


import org.example.common.response.ErrorCodeEnum;

/**
 * @author <a href="mailto:itfuyun@gmail.com">Tanxh</a>
 * @since 1.0
 */
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
