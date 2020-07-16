package org.reins.url.config;

import org.reins.url.intercepter.JWTInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class LoginConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new JWTInterceptor()).addPathPatterns("/**").excludePathPatterns("/loginReq", "/register");
//        super.addInterceptors(registry);
    }

//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")
//                .allowedOrigins("*")
//                .allowCredentials(true)
//                .allowedMethods("GET", "POST", "DELETE", "PUT", "PATCH", "OPTIONS", "HEAD")
//                .maxAge(3600 * 24);
//    }
}
//public class LoginConfig extends WebMvcConfigurationSupport {
//
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(new JWTInterceptor()).addPathPatterns("/**").excludePathPatterns("/loginReq", "/register", "/{[A-Za-z0-9]{6}}");
//        super.addInterceptors(registry);
//    }
//}