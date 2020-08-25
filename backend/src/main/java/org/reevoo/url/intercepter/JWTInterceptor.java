package org.reevoo.url.intercepter;

import org.reevoo.url.util.JwtUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class JWTInterceptor implements HandlerInterceptor {
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
        try {
            if (jwt != null) {
                if (JwtUtil.verify(jwt)) {
                    return true;
                } else {
                    response.setStatus(404);
                    return false;
                }
            }
            response.getWriter().write("未登录，请重新登录后操作");
        } catch (Exception e) {
            e.printStackTrace();
        }
        response.setStatus(404);
        return false;
    }
}
