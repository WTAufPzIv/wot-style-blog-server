package com.example.blog.model.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ResponseResult<T> {
    private int code;
    private String message;
    private T data;

    public ResponseResult(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> ResponseResult<T> success(T data) {
        return new ResponseResult<>(1, "SUCCESS", data);
    }

    public static  ResponseResult<Void> error(int code, String message) {
        return new ResponseResult<>(code, message, null);
    }
}