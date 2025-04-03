package com.example.blog.aspect;

import com.example.blog.common.exception.BusinessException;
import com.example.blog.entity.Admin;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import com.example.blog.annotation.LoginCheck;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
@Slf4j
public class LoginCheckAspect {
    @Around("@annotation(loginCheck)")
    public Object checkLogin(ProceedingJoinPoint joinPoint, LoginCheck loginCheck) throws Throwable {
        HttpSession session = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest().getSession();

        Admin admin = (Admin) session.getAttribute("currentUser");
        if (loginCheck.required() && admin == null) {
            throw new BusinessException(403, "账号未登录");
        }
        return joinPoint.proceed();
    }
}
