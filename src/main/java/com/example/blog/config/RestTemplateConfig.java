package com.example.blog.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate(
            new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory())
        );

        // 可选的配置项（根据需求添加）：
        // 1. 添加消息转换器
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        // 2. 设置错误处理器
        // restTemplate.setErrorHandler(new CustomErrorHandler());

        return restTemplate;
    }
}

