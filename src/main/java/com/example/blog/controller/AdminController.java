package com.example.blog.controller;

import com.example.blog.annotation.RequestBodyValid;
import com.example.blog.entity.Admin;
import com.example.blog.model.dto.ResponseResult;
import com.example.blog.service.AdminService;
import com.example.blog.model.vo.adminVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auroraWeb")
public class AdminController {
    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }


    @PostMapping(value = "/adminLogin", consumes = {MediaType.TEXT_PLAIN_VALUE})
    public ResponseResult<Admin> login(@RequestBodyValid(targetClass = adminVO.class, fields = {"username", "password"}) adminVO admin, HttpSession session) throws JsonProcessingException {
        return adminService.login(admin.getUsername(), admin.getPassword(), session);
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