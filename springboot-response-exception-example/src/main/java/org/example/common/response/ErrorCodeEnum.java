package org.example.common.response;

import org.springframework.http.HttpStatus;

/**
 * 系统错误码枚举类
 *
 * @author <a href="mailto:itfuyun@gmail.com">Tanxh</a>
 * @since 1.0
 */
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
