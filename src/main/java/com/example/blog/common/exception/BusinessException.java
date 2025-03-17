package com.example.blog.common.exception;

import lombok.Getter;

// 自定义业务异常类（放在 exception 包）
@Getter
public class BusinessException extends RuntimeException {
    private final int code;

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }
}
