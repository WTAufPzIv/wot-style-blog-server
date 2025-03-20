package com.example.blog.common.utils;

import com.example.blog.common.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import java.security.MessageDigest;

@Slf4j
public class MD5WithSalt {
    private static String SALT = "AuroraAksnesOs@icloud.com";

    // MD5加密带盐
    public static String md5HashWithSalt(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            String saltedInput = input + SALT; // 盐与原始字符串拼接
            byte[] hashBytes = md.digest(saltedInput.getBytes("UTF-8"));
            return bytesToHex(hashBytes);
        } catch (Exception e) {
            throw new BusinessException(401, "用户校验失败");
        }
    }

    // 字节转十六进制字符串
    private static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xFF & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

    // 验证密码
    public static boolean verifyPassword(String input, String storedHash) {
        String hashedInput = md5HashWithSalt(input);
        log.info(md5HashWithSalt("Cxxh19981014@"));
        return storedHash.equals(hashedInput);
    }
}
