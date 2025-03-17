package com.example.blog.controller;

import com.example.blog.entity.Admin;
import com.example.blog.model.dto.ResponseResult;
import com.example.blog.service.AdminService;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class AdminController {
    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }


    @PostMapping("/adminLogin")
    public ResponseResult<Admin> login(@RequestBody Map<String, String> loginReq, HttpSession session) {
        return adminService.login(loginReq.get("username"), loginReq.get("password"), session);
    }

    @PostMapping("/adminLogout")
    public ResponseResult<String> logout(HttpSession session) {
        return adminService.logout(session);
    }
}