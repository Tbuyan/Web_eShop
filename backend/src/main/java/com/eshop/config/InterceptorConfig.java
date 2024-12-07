package com.eshop.config;

import com.eshop.interceptor.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 拦截器的配置，写好的拦截器需要在此注册才能使用
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {


    public LoginInterceptor loginInterceptor(){return new LoginInterceptor();}

    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor())
                .addPathPatterns("/**")  // 拦截所有请求
                .excludePathPatterns("/**/user/**");  // 排除特定路径
    }

}
