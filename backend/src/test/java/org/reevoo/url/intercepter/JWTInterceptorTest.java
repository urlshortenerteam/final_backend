package org.reevoo.url.intercepter;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.runner.RunWith;
import org.reevoo.url.ApplicationTests;
import org.reevoo.url.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponseWrapper;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JWTInterceptorTest extends ApplicationTests {
    @Test
    public void contextLoads() {
    }

    @Autowired
    private JWTInterceptor jwtInterceptor;

    @Before
    public void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    public void afterCompletion() {
        jwtInterceptor.afterCompletion(null, null, null, null);
    }

    @Test
    public void postHandle() {
        jwtInterceptor.postHandle(null, null, null, null);
    }

    @Test
    public void preHandle() {
        HttpServletResponseWrapper responseWrapper = new HttpServletResponseWrapper(Objects.requireNonNull(((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getResponse()));

        HttpServletRequestWrapper requestWrapper = new HttpServletRequestWrapper(((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest()) {
            @Override
            public String getMethod() {
                return "OPTIONS";
            }
        };
        assertTrue(jwtInterceptor.preHandle(requestWrapper, responseWrapper, null));

        requestWrapper = new HttpServletRequestWrapper(((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest());
        assertFalse(jwtInterceptor.preHandle(requestWrapper, responseWrapper, null));
        assertEquals(404, responseWrapper.getStatus());

        requestWrapper = new HttpServletRequestWrapper(((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest()) {
            @Override
            public String getHeader(String name) {
                if (name.equals("Authorization")) return JwtUtil.sign(1, "ao7777", 0, false);
                return null;
            }
        };
        assertTrue(jwtInterceptor.preHandle(requestWrapper, responseWrapper, null));

        requestWrapper = new HttpServletRequestWrapper(((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest()) {
            @Override
            public String getHeader(String name) {
                if (name.equals("Authorization")) return "";
                return null;
            }
        };
        assertFalse(jwtInterceptor.preHandle(requestWrapper, responseWrapper, null));
        assertEquals(404, responseWrapper.getStatus());
    }
}
