package com.rzdata.ps.exception;

import cn.hutool.core.util.StrUtil;
import com.rzdata.core.domain.AjaxResult;
import com.rzdata.exception.DemoModeException;
import com.rzdata.exception.ServiceException;
import com.rzdata.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;

/**
 * 全局异常处理器
 *
 * @author ruoyi
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 权限校验异常
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(AccessDeniedException.class)
    public ErrorInfo handleAccessDeniedException(AccessDeniedException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("权限校验异常，错误原因：{}，异常信息：{}", e.getMessage(), e);
        return ErrorInfo.result(HttpStatus.BAD_REQUEST.value(),"权限校验错误", "没有权限，请联系管理员授权");
    }

    /**
     * 请求方式不支持
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ErrorInfo handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException e,
                                                         HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("请求方式异常，错误原因：{}，异常信息：{}", e.getMessage(), e);
        return ErrorInfo.result(HttpStatus.BAD_REQUEST.value(), "请求错误", "请求方式不支持");
    }

    /**
     * 业务异常
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ServiceException.class)
    public AjaxResult handleServiceException(ServiceException e, HttpServletRequest request) {
        log.error("业务异常，错误原因：{}，异常信息：{}", e.getMessage(), e);
        Integer code = e.getCode();
        return StringUtils.isNotNull(code) ? AjaxResult.error(code, "服务器开小差了，请稍后再试或联系管理员") : AjaxResult.error("服务器开小差了，请稍后再试或联系管理员");
    }

    /**
     * 拦截未知的运行时异常
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(RuntimeException.class)
    public ErrorInfo handleRuntimeException(RuntimeException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("拦截未知的运行时异常，错误原因：{}，异常信息：{}", e.getMessage(), e);
        return ErrorInfo.result(org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "服务器开小差了，请稍后再试或联系管理员", "请求地址'{}',发生未知异常.", requestURI);
    }

    /**
     * 系统异常
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = Exception.class)
    public ErrorInfo handlerException(Exception e) {
        log.error("系统异常，错误原因：{}，异常信息：{}", e.getMessage(), e);
        return ErrorInfo.result(org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "服务器开小差了，请稍后再试或联系管理员", "服务器开小差了，请稍后再试或联系管理员");
    }

    /**
     * 自定义验证异常
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = BindException.class)
    public ErrorInfo bindException(BindException e) {
        log.error("自定义验证异常，错误原因：{}，异常信息：{}", e.getMessage(), e);
        StringBuilder builder = new StringBuilder();
        for (FieldError fieldError : e.getFieldErrors()) {
            if (StrUtil.isNotBlank(builder.toString())) {
                builder.append(",");
            }
            builder.append(fieldError.getField()).append(" 字段值不能 ").append(fieldError.getRejectedValue());
        }
        String msg = "参数类型不匹配";
        return ErrorInfo.result(org.springframework.http.HttpStatus.BAD_REQUEST.value(), builder.toString(), msg);
    }

    /**
     * 参数校验异常
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public ErrorInfo constraintViolationException(ConstraintViolationException e) {
        log.error("参数校验异常，错误原因：{}，异常信息：{}", e.getMessage(), e);
        String message = e.getConstraintViolations().iterator().next().getMessage();
        return ErrorInfo.result(org.springframework.http.HttpStatus.BAD_REQUEST.value(), "参数校验异常",  "");
    }

    /**
     * 方法参数验证异常
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorInfo handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("方法参数验证异常，错误原因：{}，异常信息：{}", e.getMessage(), e);
        String message = e.getBindingResult().getFieldError().getDefaultMessage();
        return ErrorInfo.result(org.springframework.http.HttpStatus.BAD_REQUEST.value(), "方法参数验证异常",  "");
    }

    /**
     * 接口请求异常
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = RestException.class)
    public ErrorInfo handlerRestException(RestException e) {
//        log.error("接口请求异常，错误原因：{}，异常信息：{}", e.getMessage(), e);
        return ErrorInfo.result(e.getErrCode(), "接口请求异常", e.getMessage(), "");
    }

    /**
     * 未找到数据异常
     */
    @ResponseStatus(HttpStatus.FOUND)
    @ExceptionHandler(value = DataNotFoundException.class)
    public ErrorInfo handlerCollectException(DataNotFoundException e) {
        return ErrorInfo.result(e.getErrCode(), "未找到对应的数据", e.getMessage(), "");
    }

    /**
     * 演示模式异常
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DemoModeException.class)
    public AjaxResult handleDemoModeException(DemoModeException e) {
        return AjaxResult.error("演示模式，不允许操作");
    }
}
