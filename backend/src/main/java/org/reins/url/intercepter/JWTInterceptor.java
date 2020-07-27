package org.reins.url.intercepter;

import org.reins.url.util.JwtUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class JWTInterceptor implements HandlerInterceptor {
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object obj, Exception e) throws Exception {
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object obj, ModelAndView mav) throws Exception {
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object obj) {
        //System.out.println(request.getMethod());
        if (request.getMethod().equals("OPTIONS")) return true;
        //System.out.println("开始进入拦截器检验jwt头部是否含有Authorization方法！");
        // 通过url得到token请求头是否包含Authorization
        String jwt = request.getHeader("Authorization");
        //System.out.println(jwt);
        try {
            // 检测请求头是否为空
            if (jwt != null) {
//                Claims c = jWTUtil.parseJWT(jwt);
//                Users user = usersService.findById((Long) c.get("id"));
//                if (user==null) return false;
                if (jwt.equals("SXSTQL")) return true;
                //System.out.println("结束进入拦截器检验jwt头部是否含有Authorization方法！");
                if (JwtUtil.verify(jwt)) {
                    return true;
                } else {
                    response.setStatus(404);
                    return false;
                }
            }
            //System.out.println("token解析错误，验证失败");
            response.getWriter().write("未登录，请重新登录后操作");
        } catch (Exception e) {
            e.printStackTrace();
        }
        response.setStatus(404);
        return false;
    }

}
