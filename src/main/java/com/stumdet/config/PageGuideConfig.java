package com.stumdet.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class PageGuideConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {

        registry.addViewController("/").setViewName("index");
        registry.addViewController("/index").setViewName("index");
        registry.addViewController("/user/login").setViewName("user/login/index");
        registry.addViewController("/user").setViewName("user/index");
        registry.addViewController("/items").setViewName("items/index");
        registry.addViewController("/items/projects").setViewName("items/projects/index");
        registry.addViewController("/items/projects/tasks").setViewName("items/projects/tasks/index");
        registry.addViewController("/dashboard").setViewName("dashboard/index");
        registry.addViewController("/cesium").setViewName("cesium/index");
        registry.addViewController("/admin").setViewName("admin/index");
    }

    /**
     * 这里的路径需要修改
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/api/imagerepo/**").addResourceLocations("file:///C:/Stu/StumdetRoot/ImageRepo/");
        registry.addResourceHandler("/api/odmoutput/**").addResourceLocations("file:///C:/Stu/StumdetRoot/ODMOutputRoot/");
    }
}
