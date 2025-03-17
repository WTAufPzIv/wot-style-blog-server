package com.example.blog.filter;

import com.example.blog.common.utils.RsaUtils;
import com.example.blog.filter.wrapper.DecryptRequestWrapper;
import org.springframework.beans.factory.annotation.Value;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;

@WebFilter(urlPatterns = "/*")
public class DecryptFilter implements Filter {

    @Value("${rsa.private-key}")
    private String privateKey;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;

        // 读取加密请求体
        StringBuilder encryptedBody = new StringBuilder();
        try (BufferedReader reader = httpRequest.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                encryptedBody.append(line);
            }
        }

        // 解密处理
        String decryptedBody;
        try {
            decryptedBody = RsaUtils.decrypt(encryptedBody.toString(), privateKey);
        } catch (Exception e) {
            throw new ServletException("解密失败", e);
        }

        // 替换请求体
        DecryptRequestWrapper wrappedRequest = new DecryptRequestWrapper(httpRequest, decryptedBody);
        chain.doFilter(wrappedRequest, response);
    }
}
