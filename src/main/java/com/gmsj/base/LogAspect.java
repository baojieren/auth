package com.gmsj.base;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmsj.common.constant.HttpHeaderConstant;
import com.gmsj.common.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

/**
 * @author renbaojie
 */
@Aspect
@Component
@Slf4j
public class LogAspect {
    @Resource
    ObjectMapper objectMapper;

    private static final ThreadLocal<Map<String, Object>> THREAD_LOCAL = ThreadLocal.withInitial(HashMap::new);

    @Pointcut("execution(public * com.gmsj.web.*.*(..))")
    public void webLog() {
    }

    @Before("webLog()")
    public void theBefore(JoinPoint joinPoint) {
    }

    @After("webLog()")
    public void theAfter() {
    }

    @Around("webLog()")
    public Object theAround(ProceedingJoinPoint joinPoint) throws Throwable {
        Map<String, Object> threadLocalMap = THREAD_LOCAL.get();
        threadLocalMap.put("startTime", System.currentTimeMillis());

        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert requestAttributes != null;
        HttpServletRequest request = requestAttributes.getRequest();
        HttpServletResponse response = requestAttributes.getResponse();

        String authorization = request.getHeader(HttpHeaderConstant.HEADER_AUTH);
        String traceId = request.getHeader(HttpHeaderConstant.HEADER_TRACEID);
        String ip = request.getHeader(HttpHeaderConstant.HEADER_IP);
        String requestURL = request.getRequestURL().toString();
        String requestURI = request.getRequestURI();
        String method = request.getMethod();
        String queryString = request.getQueryString();
        Object[] args = joinPoint.getArgs();

        // 解析token拿用户id
        String userId = null;
        if (!StringUtils.isEmpty(authorization) && authorization.startsWith(HttpHeaderConstant.HEADER_SCHEME)) {
            try {
                String token = authorization.split(" ")[1];
                userId = JwtUtil.getUserId(token);
            } catch (Exception e) {
                log.error("解析token拿userId出错 {}", e.toString());
            }
        }

        // 请求参数
        StringBuilder params = new StringBuilder();
        if (HttpMethod.GET.toString().equals(method)) {
            if (!StringUtils.isEmpty(queryString)) {
                params = new StringBuilder(URLDecoder.decode(queryString, "UTF-8"));
            }
        } else {
            if (!ObjectUtils.isEmpty(args)) {
                for (Object arg : args) {
                    if (arg instanceof ServletRequest || arg instanceof ServletResponse || arg instanceof MultipartFile) {
                        continue;
                    }
                    params.append(objectMapper.writeValueAsString(arg));
                }
            }
        }

        threadLocalMap.put("userId", userId);
        threadLocalMap.put("traceId", traceId);
        threadLocalMap.put("ip", ip);
        threadLocalMap.put("url", requestURI);

        log.info("请求--->: {},\ttraceId: {},\tuserId: {},\tIP: {},\t参数: {}"
                , threadLocalMap.get("url")
                , threadLocalMap.get("traceId")
                , threadLocalMap.get("userId")
                , threadLocalMap.get("ip")
                , params.toString());

        // 执行实际方法，result为方法执行返回值
        Object result = joinPoint.proceed();

        // 把traceId设置到响应头
        if (!ObjectUtils.isEmpty(response)) {
            response.addHeader("traceId", threadLocalMap.get("traceId") == null ? "" : threadLocalMap.get("traceId").toString());
        }

        log.info("响应<---: {},\ttraceId: {},\tuserId: {},\t耗时: {}ms,\t结果: {}"
                , threadLocalMap.get("url")
                , threadLocalMap.get("traceId")
                , threadLocalMap.get("userId")
                , System.currentTimeMillis() - (long) threadLocalMap.get("startTime")
                , objectMapper.writeValueAsString(result));

        THREAD_LOCAL.remove();
        return result;
    }
}
