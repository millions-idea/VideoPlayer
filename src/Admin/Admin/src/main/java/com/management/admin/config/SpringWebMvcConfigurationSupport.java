package com.management.admin.config;

import com.management.admin.interceptor.DomainAuthenticationInterceptor;
import com.management.admin.interceptor.WebMvcOperationLogInterceptor;
import com.management.admin.interceptor.WxAuthenticationInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
public class SpringWebMvcConfigurationSupport extends WebMvcConfigurationSupport {

    /**
     * 注入系统日志-isOpen可以控制日志系统的开闭
     * @return
     */
    @Bean
    public WebMvcOperationLogInterceptor WebMvcOperationLogInterceptor() {
        return new WebMvcOperationLogInterceptor(true);
    }

    @Bean
    public DomainAuthenticationInterceptor DomainAuthenticationInterceptor() {
        return new DomainAuthenticationInterceptor();
    }

    @Bean
    public WxAuthenticationInterceptor WxAuthenticationInterceptor() {
        return new WxAuthenticationInterceptor(true);
    }

    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        // 财务模块
        registry.addInterceptor(WebMvcOperationLogInterceptor()).addPathPatterns("/**");
        registry.addInterceptor(DomainAuthenticationInterceptor()).addPathPatterns("/**");
        registry.addInterceptor(WxAuthenticationInterceptor()).addPathPatterns("/**");
        super.addInterceptors(registry);
    }


    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/")
                .addResourceLocations("classpath:/templates/");
        super.addResourceHandlers(registry);
    }


}