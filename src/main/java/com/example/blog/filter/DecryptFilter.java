package com.example.blog.filter;

import com.example.blog.common.utils.RsaUtils;
import com.example.blog.filter.wrapper.DecryptRequestWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import java.io.BufferedReader;
import java.io.IOException;

@Component
@Slf4j
public class DecryptFilter implements Filter {
    static String privateKeyStr = "-----BEGIN PRIVATE KEY-----\n" +
            "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDGHh64wPlqxWLf\n" +
            "V7ZMBE7YezVvZja+YsqQU2n57V7uuSJ5edukQlxwLmS4RKXdjkDuxuOqukaltcRe\n" +
            "fg7ZWnnor92iJe9rN8xnS9MIl1tz5JsTNZTvWsJk1FID+ls8LewMyuV1m88YKtnR\n" +
            "SGdHT7591urZG892x8g7ABxor0N7cT33g5hKotzj1LHzrBv/RmWE5a+ARefcBw5K\n" +
            "otrIgYrHAD6/M6tFICdei6otSF0Ee/DM2YDVO6HSJYw3IbH/6hEGmNw7OSuZt7Zn\n" +
            "wDrPlIyXdtnCl3GY3ZMZAlK07SGZmuQc1x/yuC5UlNWUZW3z6uKk63sfCe9PphWt\n" +
            "xavB4oIPAgMBAAECggEAHQpUOYO76700QYXDaIAnKI9i5Unn0196zs4TVjnHtRl5\n" +
            "JrJIklrR3vXF+MbDQ2czOGz5+VAlw+uRdcJnnc+n2AZTaA3EewRqV2fjbrp1Atdr\n" +
            "LdUoWC+HGC2ROhkMH5WhqLxjByrIIARD+mvxC6ByTVLgCOQNNJ2TSaa6A1RRgXn1\n" +
            "Y7MGzrOUF2SfDShOGKW/poBAlGvGinpf0dulOdGsGA0GG/VII9UEHm0azAIgUpIo\n" +
            "CdFZLbA0ZgbyXzkUcPVoY+JCrkbx+UG2b21mgqlLgA8toOsPY60K7C6j29YP/kjm\n" +
            "0s9JVaUDXZGMqno/ArEGRioBheWbY08z9S2sJXa0PQKBgQDrSutPXbfABE9/uhqb\n" +
            "HKzZCT/wjRzh7VVuIFSN3al8ZJbT/8MFVTCd3uFyhjkxBeMOh1qMZ8N7Lob50MTV\n" +
            "Xy2Yt43HkAojqVO5348DyGerAFb5Arq5TpICjZdj6rPLCok0e2+MgcDL7nhsAVwP\n" +
            "59HbORGoZBiUsvNKssOnaayQXQKBgQDXjah13L7ogmQZnuq6x9Jy7wMNzGrAxcAg\n" +
            "vRnJgCivFT9f4CrdvIa+IZpGi9AnAYdz6CmBjsiB7l94esN3zUG6/Tpd847aT6ry\n" +
            "Vc3YCZDIWDqgzbN8fAKO34F59oeHnBb5EYaXUgtv674mZHhwbFM0L5qsnKqe9Gjx\n" +
            "Q7BDf4jlWwKBgQCY2ayPfGQ4GxTB9tX92dz3iSi+Tg1rP1B2IA6Nsme95WKOCeJY\n" +
            "/oR8qiNu0H8qt5ZPA+Lwvx+gUBNjCho0OoZWZTd3Yr0TeZZoczBm1rNxxOC7FujS\n" +
            "JJZ5jh3AzOFLj2aeb2mBr3Ddiec00pVJx+DuNt6fs5a8Upv19LdhdCakCQKBgQDA\n" +
            "uIvszS/3LFWZPqe51dzIHACL/r9XmbbczpXWvfoiCApAi2loFR4Xsm2AM5MovMpB\n" +
            "jEpH5sRIvzqmuoQ3gdIKAEyGIbQQZnh98M95H5xOM+w/R4r5etrCqPzXcd7dFWbI\n" +
            "y62db6KAL7M0GlJAmemOWB2fxPcb5YRKae2Xe9iGzwKBgHv2VYbCOBLtcOTINnsi\n" +
            "EPcxuqOrCW5yp8Q30A+m9vbeqVtXk0UVewTUSvfVkWMLyplzdkeRq1D+wQ4Li9xS\n" +
            "mIqyPeiR9iHMqLtMspFT52pleYfrZUh2EZsYGJA2brd8+Jk734AkkViQ3VnQudEo\n" +
            "hswMaAkz506iRieG/+uzP/bN\n" +
            "-----END PRIVATE KEY-----";

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
            decryptedBody = RsaUtils.decrypt(encryptedBody.toString(), RsaUtils.loadPrivateKey(privateKeyStr));
        } catch (Exception e) {
            throw new ServletException("解密失败", e);
        }

        DecryptRequestWrapper wrappedRequest = new DecryptRequestWrapper(httpRequest, decryptedBody);
        chain.doFilter(wrappedRequest, response);
    }
}
