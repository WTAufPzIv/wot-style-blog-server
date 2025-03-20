package com.example.blog.resolver;

import com.example.blog.annotation.RequestBodyValid;
import com.example.blog.common.exception.BusinessException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.MethodParameter;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;

public class RequestBodyValidArgumentResolver implements HandlerMethodArgumentResolver {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(RequestBodyValid.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        if (request == null) {
            throw new IllegalStateException("No HttpServletRequest");
        }
        // 读取请求体
        String requestBody = StreamUtils.copyToString(request.getInputStream(), StandardCharsets.UTF_8);
        RequestBodyValid annotation = parameter.getParameterAnnotation(RequestBodyValid.class);
        if (annotation == null) {
            throw new IllegalStateException("@RequestBodyValid annotation is missing");
        }

        // 转换为目标对象
        Class<?> targetClass = annotation.targetClass();
        Object targetObject = objectMapper.readValue(requestBody, targetClass);

        // 校验指定字段非空
        String[] fieldsToValidate = annotation.fields();
        for (String fieldName : fieldsToValidate) {
            validateField(targetObject, targetClass, fieldName);
        }

        return targetObject;
    }

    private void validateField(Object targetObject, Class<?> targetClass, String fieldName) {
        try {
            Field field = targetClass.getDeclaredField(fieldName);
            field.setAccessible(true);
            Object value = field.get(targetObject);
            if (value == null || (value instanceof String && ((String) value).isEmpty())) {
                throw new BusinessException(400, "请求参数错误: 字段 " + fieldName + " 不能为空");
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new BusinessException(500, "服务器内部错误: 字段校验配置错误");
        }
    }
}