package org.reevoo.url.logging;

import com.alibaba.fastjson.JSONObject;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Component
@Aspect
public class LogAspect {
    private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);

    @Pointcut("execution(public * org.reevoo.url.controller..*.*(..))")
    public void pointCut() {
    }

    @Before("pointCut()")
    public void beforeMethod(JoinPoint joinPoint) {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        assert servletRequestAttributes != null;
        HttpServletRequest request = servletRequestAttributes.getRequest();

        JSONObject message = new JSONObject();
        message.put("remoteAddr", request.getRemoteAddr());
        message.put("requestURI", request.getRequestURI());
        message.put("controller", joinPoint.getTarget().getClass());
        message.put("method type", request.getMethod());
        message.put("req paras", JSONObject.toJSONString(joinPoint.getArgs()));

        logger.info(message.toJSONString());
    }
}
