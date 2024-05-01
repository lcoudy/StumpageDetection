package com.stumdet.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@SuppressWarnings("deprecation")
@SpringBootConfiguration
public class SpringMvcConfig extends WebMvcConfigurerAdapter{
    @Autowired
    private FilterConfig filterConfig;

    @Bean
    public HandlerInterceptor getLoginHandlerIntercepter() {
        return new LoginHandlerIntercepter();
    }

    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(filterConfig).addPathPatterns("/**");
        registry.addInterceptor(getLoginHandlerIntercepter())
                .addPathPatterns("/**")
                .excludePathPatterns("/api/token-auth")
                .excludePathPatterns("/")
                .excludePathPatterns("/index")
                .excludePathPatterns("/**")
                .excludePathPatterns("/css/**")
                .excludePathPatterns("/js/**")
                .excludePathPatterns("/img/**")
                .excludePathPatterns("/upload/upload");
    }
}