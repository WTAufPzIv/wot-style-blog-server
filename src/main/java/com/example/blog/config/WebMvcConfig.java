package com.example.blog.config;

import com.example.blog.resolver.DecryptRequestBodyResolver;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.validation.Validator;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {
    private final ObjectMapper objectMapper;
    private final Validator validator;

    @Bean
    public DecryptRequestBodyResolver decryptRequestBodyResolver() {
        return new DecryptRequestBodyResolver(objectMapper, validator);
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(decryptRequestBodyResolver());
    }
}
