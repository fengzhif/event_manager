package com.fengzhi.event_manager.config;

import com.fengzhi.event_manager.interceptor.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private LoginInterceptor loginInterceptor;

    @Autowired
    public WebConfig(LoginInterceptor loginInterceptor) {
        this.loginInterceptor = loginInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
                .excludePathPatterns("/user/login", "/user/register", "/user/forgotPassword", "/user/resetPassword");
        //放行登录和注册接口 忘记密码和重置密码接口
    }

//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        // 将 /images/** 映射到本地图片文件夹
//        registry.addResourceHandler("/images/**")
//                .addResourceLocations("file:D:/tmp_project/vue_learn/image_store_for_eventManager/");
//    }

}
