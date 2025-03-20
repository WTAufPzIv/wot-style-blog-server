package com.example.blog.config;

import com.example.blog.filter.DecryptFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<DecryptFilter> decryptionFilter() throws Exception {
        FilterRegistrationBean<DecryptFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new DecryptFilter());
        registration.addUrlPatterns("/auroraWeb/*"); // 需要解密的路径
        registration.setEnabled(false); // 防止自定义过滤器出现在全局过滤器中
        registration.setOrder(Ordered.HIGHEST_PRECEDENCE); // 最高优先级
        return registration;
    }
}
