package com.example.blog.entity;

public class ResponseResult {
    private int code;
    private String message;
    private Object data;

    // 全参构造函数（必须显式声明）
    public ResponseResult(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public ResponseResult() {
    }

    // 静态工厂方法
    public static ResponseResult success(Object data) {
        return new ResponseResult(1, "Success", data);
    }

    public static ResponseResult error(int code, String message) {
        return new ResponseResult(code, message, null);
    }
}