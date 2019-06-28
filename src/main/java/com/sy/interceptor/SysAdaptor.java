package com.sy.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

@Configuration
public class SysAdaptor implements WebMvcConfigurer {


    @Resource(name = "loginInterceptor")
    private HandlerInterceptor handlerInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        //拦截器
        InterceptorRegistration interceptorRegistration = registry.addInterceptor(handlerInterceptor);
        //拦截规则
        interceptorRegistration.addPathPatterns("/**");
        //不拦截的URL
        interceptorRegistration.excludePathPatterns("/","/login.html","/main.html","/statics/**");


    }
}
