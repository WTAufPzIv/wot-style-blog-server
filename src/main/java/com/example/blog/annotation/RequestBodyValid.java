package com.example.blog.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestBodyValid {
    Class<?> targetClass(); // 目标实体类
    String[] fields() default {}; // 需要校验的字段，默认为空即不校验
}
