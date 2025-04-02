package com.example.blog.annotation;

import jakarta.validation.Valid;

import java.lang.annotation.*;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Valid  // 关键！必须包含校验注解
public @interface RequestBodyValid {
    Class<?> targetClass(); // 目标实体类
    String[] fields() default {}; // 需要校验的字段，默认为空即不校验
}
