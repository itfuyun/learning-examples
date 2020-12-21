package org.example.common.response;

/**
 * @author <a href="mailto:itfuyun@gmail.com">Tanxh</a>
 * @since 1.0
 */
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
