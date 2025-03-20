package com.example.blog.service;

import com.example.blog.common.exception.BusinessException;
import com.example.blog.common.utils.MD5WithSalt;
import com.example.blog.entity.Admin;
import com.example.blog.model.dto.ResponseResult;
import com.example.blog.repository.AdminRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

@Service
public class AdminService {
    private final AdminRepository repository;

    public AdminService(AdminRepository repository) {
        this.repository = repository;
    }

    public ResponseResult<Admin> login(String username, String password, HttpSession session) {
        Admin user = repository.findByUsername(username)
                .orElseThrow(() -> new BusinessException(500, "用户不存在"));

        if (!MD5WithSalt.verifyPassword(password, user.getPassword())) {
            throw new BusinessException(500, "密码错误");
        }

        // 存储会话信息
        session.setAttribute("currentUser", user);
        return ResponseResult.success(user);
    }

    public ResponseResult<String> logout(HttpSession session) {
        try {
            session.invalidate(); // 销毁会话
            return ResponseResult.success("退出成功");
        } catch (Exception e) {
            throw new BusinessException(500, "退出登录失败" + e.getMessage());
        }

    }

    public ResponseResult<Admin> checkLogin(HttpSession session) {
        Admin admin = (Admin) session.getAttribute("currentUser");
        if (admin == null) {
            throw new BusinessException(403, "账号未登录");
//            return ResponseResult.success(null);
        } else {
            return ResponseResult.success(admin);
        }
    }
}