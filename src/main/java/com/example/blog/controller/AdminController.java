package com.example.blog.controller;

import com.example.blog.annotation.DecryptRequestBody;
import com.example.blog.annotation.RequestBodyValid;
import com.example.blog.entity.Admin;
import com.example.blog.model.dto.ResponseResult;
import com.example.blog.model.vo.AdminVO;
import com.example.blog.service.AdminService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/auroraWeb")
public class AdminController {
    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }


    @PostMapping(value = "/adminLogin")
    public ResponseResult<Admin> login(
            @DecryptRequestBody
                    @Valid
            AdminVO admin, HttpSession session
    ) {
        log.info(admin.getUsername());
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