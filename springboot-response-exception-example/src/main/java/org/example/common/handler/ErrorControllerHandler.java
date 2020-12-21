package org.example.common.handler;

import org.example.common.response.ErrorCodeEnum;
import org.example.common.response.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author <a href="mailto:itfuyun@gmail.com">Tanxh</a>
 * @since 1.0
 */
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
