package com.example.blog.controller;

import com.example.blog.common.exception.BusinessException;
import com.example.blog.entity.Admin;
import com.example.blog.model.dto.ResponseResult;
import com.example.blog.service.AdminService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auroraWeb")
public class AdminController {
    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }


    @PostMapping(value = "/adminLogin", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public ResponseResult<Admin> login(@RequestParam(value = "username", required = false) String username, @RequestParam(value = "password", required = false) String password, HttpSession session) {
        if (username == null || password == null || username.isEmpty() || password.isEmpty()) {
            throw new BusinessException(401, "请求参数错误");
        }
        return adminService.login(username, password, session);
    }


    @PostMapping("/adminLogout")
    public ResponseResult<String> logout(HttpSession session) {
        return adminService.logout(session);
    }

    @PostMapping("/adminIsLogin")
    public ResponseResult<Admin> isLogin(HttpSession session) {
        return adminService.checkLogin(session);
    }
}