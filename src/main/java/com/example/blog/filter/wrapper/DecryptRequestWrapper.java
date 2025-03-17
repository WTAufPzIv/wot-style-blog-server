package com.example.blog.filter.wrapper;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;

public class DecryptRequestWrapper extends HttpServletRequestWrapper {

    private final String decryptedBody;

    public DecryptRequestWrapper(HttpServletRequest request, String decryptedBody) {
        super(request);
        this.decryptedBody = decryptedBody;
    }

    @Override
    public ServletInputStream getInputStream() {
        ByteArrayInputStream bis = new ByteArrayInputStream(decryptedBody.getBytes());
        return new ServletInputStream() {
            @Override public boolean isFinished() { return bis.available() == 0; }
            @Override public boolean isReady() { return true; }
            @Override public void setReadListener(ReadListener listener) {}
            @Override public int read() { return bis.read(); }
        };
    }

    @Override
    public BufferedReader getReader() {
        return new BufferedReader(new InputStreamReader(this.getInputStream()));
    }
}