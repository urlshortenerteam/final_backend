package org.reevoo.url.intercepter;

import com.alibaba.fastjson.JSONObject;
import org.reevoo.url.logging.LogAspect;
import org.reevoo.url.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class JWTInterceptor implements HandlerInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object obj, Exception e) {
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object obj, ModelAndView mav) {
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object obj) {
        if (request.getMethod().equals("OPTIONS")) return true;
        String jwt = request.getHeader("Authorization");
        JSONObject message = new JSONObject();
        try {
            if (jwt != null) {
                if (JwtUtil.verify(jwt)) {
                    return true;
                } else {
                    message.put("message", "Invalid JWT");
                    message.put("JWT", jwt);
                    logger.info(message.toJSONString());
                    response.setStatus(404);
                    return false;
                }
            }
            response.getWriter().write("未登录，请重新登录后操作");
        } catch (Exception e) {
            e.printStackTrace();
        }
        message.put("message", "Missing JWT");
        logger.info(message.toJSONString());
        response.setStatus(404);
        return false;
    }
}
