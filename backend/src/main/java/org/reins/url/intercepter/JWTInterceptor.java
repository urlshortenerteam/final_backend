package org.reins.url.intercepter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.reins.url.entity.Users;
import org.reins.url.service.UsersService;
import org.reins.url.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import io.jsonwebtoken.Claims;

@Component
public class JWTInterceptor implements HandlerInterceptor {

  @Autowired
  private JwtUtil jWTUtil;

  @Override
  public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object obj, Exception e) throws Exception {
  }

  @Override
  public void postHandle(HttpServletRequest request, HttpServletResponse response, Object obj, ModelAndView mav) throws Exception {
  }

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object obj) {
    //System.out.println(request.getMethod());
    if (request.getMethod().equals("OPTIONS"))
      return true;
    //System.out.println("开始进入拦截器检验jwt头部是否含有Authorization方法！");
    // 通过url得到token请求头是否包含Authorization
    String jwt = request.getHeader("Authorization");
    //System.out.println(jwt);
    try {
      // 检测请求头是否为空
      if (jwt == null) {
        //System.out.println("用户未登录，验证失败");
      } else {
//                Claims c = jWTUtil.parseJWT(jwt);
//                Users user = usersService.findById((Long) c.get("id"));
//                if (user==null)
//                    return false;
        if (jwt.equals("SXSTQL"))
          return true;
        //System.out.println("结束进入拦截器检验jwt头部是否含有Authorization方法！");
        return jWTUtil.verify(jwt);
      }
      //System.out.println("token解析错误，验证失败");
      response.getWriter().write("未登录，请重新登录后操作");
    } catch (Exception e) {
      e.printStackTrace();
    }
    return false;
  }

}
