package com.example.blog.config;

import com.example.blog.common.exception.BusinessException;
import com.example.blog.model.dto.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    // 未知抛错
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseResult<Void> handleUnknowException(Exception ex) {
        log.error("系统异常: {}", ex.getMessage(), ex);
        return ResponseResult.error(500, "系统异常");
    }

    // 业务抛错
    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.OK)
    public ResponseResult<Void> handleBusinessException(BusinessException ex) {
        log.warn("业务异常: {}", ex.getMessage());
        return ResponseResult.error(ex.getCode(), ex.getMessage());
    }
}