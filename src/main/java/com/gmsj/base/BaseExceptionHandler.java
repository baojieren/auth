package com.gmsj.base;

import com.gmsj.common.constant.HttpHeaderConstant;
import com.gmsj.common.dto.BaseOutDTO;
import com.gmsj.common.exception.BaseError;
import com.gmsj.common.exception.BaseRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author renbaojie
 */
@Slf4j
@RestControllerAdvice
public class BaseExceptionHandler {

    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    public BaseOutDTO methodNotSupportedExceptionHandler(HttpServletRequest request, HttpServletResponse response, Exception e) {
        log.error("接口: {} 调用失败", request.getRequestURI());
        log.error("该接口不支持:{} 请求方式", request.getMethod());
        log.error("异常信息: ", e);
        response.addHeader(HttpHeaderConstant.HEADER_TRACEID, request.getHeader(HttpHeaderConstant.HEADER_TRACEID));
        return new BaseOutDTO().fail(BaseError.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(value = HttpMessageConversionException.class)
    public BaseOutDTO httpMessageConversionExceptionHandler(HttpServletRequest request, HttpServletResponse response, Exception e) {
        log.error("接口: {} 调用失败, 参数类型错误", request.getRequestURI());
        log.error("异常信息: ", e);
        response.addHeader(HttpHeaderConstant.HEADER_TRACEID, request.getHeader(HttpHeaderConstant.HEADER_TRACEID));
        return new BaseOutDTO().fail(BaseError.PARAM_TYPE_ERR);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public BaseOutDTO paramNotValidExceptionHandler(HttpServletRequest request, HttpServletResponse response, Exception e) {
        log.error("接口: {} 调用失败, 参数校验失败", request.getRequestURI());
        log.error("异常信息: ", e);
        response.addHeader(HttpHeaderConstant.HEADER_TRACEID, request.getHeader(HttpHeaderConstant.HEADER_TRACEID));
        return new BaseOutDTO().fail(BaseError.PARAM_NOT_VALID);
    }

    @ExceptionHandler(value = BaseRuntimeException.class)
    public BaseOutDTO knownErrorHandler(HttpServletRequest request, HttpServletResponse response, BaseRuntimeException e) {
        log.error("接口: {} 调用失败 : {}", request.getRequestURI(), e.getMessage());
        response.addHeader(HttpHeaderConstant.HEADER_TRACEID, request.getHeader(HttpHeaderConstant.HEADER_TRACEID));
        return new BaseOutDTO().fail(new BaseError().setCode(e.getCode()).setMsg(e.getMessage()));
    }

    @ExceptionHandler(value = Exception.class)
    public BaseOutDTO defaultErrorHandler(HttpServletRequest request, HttpServletResponse response, Exception e) {
        log.error("接口: {} 调用失败", request.getRequestURI());
        log.error("异常信息: ", e);
        response.addHeader(HttpHeaderConstant.HEADER_TRACEID, request.getHeader(HttpHeaderConstant.HEADER_TRACEID));
        return new BaseOutDTO().fail(BaseError.SYS_ERR);
    }
}
