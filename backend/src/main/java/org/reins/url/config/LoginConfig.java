package org.reins.url.config;

import org.reins.url.intercepter.JWTInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

public class LoginConfig extends WebMvcConfigurationSupport {
    @Autowired
    JWTInterceptor jwtInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor).addPathPatterns("/**").excludePathPatterns("/loginReq", "/register", "/{[A-Za-z0-9]{6}}");
        super.addInterceptors(registry);
    }
}
