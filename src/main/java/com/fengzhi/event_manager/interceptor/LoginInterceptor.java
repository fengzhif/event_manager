package com.fengzhi.event_manager.interceptor;

import com.fengzhi.event_manager.utils.JwtUtil;
import com.fengzhi.event_manager.utils.ThreadLocalUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;

@Component
public class LoginInterceptor implements HandlerInterceptor {
    StringRedisTemplate stringRedisTemplate;
    @Autowired
    public LoginInterceptor(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) throws Exception {
        String token = request.getHeader("Authorization");
//        String uri = request.getRequestURI();
//        if (uri.startsWith("/images/")) {
//            return true; // 放行图片请求
//        }
        try {
            Map<String, Object> claim = JwtUtil.parseToken(token);
            String redisToken = stringRedisTemplate.opsForValue().get(token);
            if(redisToken == null) {
                throw new RuntimeException();
            }
            ThreadLocalUtil.set(claim);
            return true;
        } catch (Exception e) {
            response.setStatus(401);
            return false;
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        ThreadLocalUtil.remove();
    }
}
