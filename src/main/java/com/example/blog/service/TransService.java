package com.example.blog.service;

import com.example.blog.common.exception.BusinessException;
import com.example.blog.model.dto.ResponseResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TransService {
    // 注入RestTemplate和ObjectMapper
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public TransService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public ResponseResult<String> handleTrans(String string) {
        try {
            return ResponseResult.success(doTrans(string));
        } catch (Exception e) {
            throw new BusinessException(500, "翻译失败: " + e.getMessage());
        }
    }

    public String doTrans(String text) {
        try {
            String url = "https://open.hunyuan.tencent.com/openapi/v1/agent/chat/completions";
            String token = "Bearer YUbSXO0pJiZZTuLl7scNF1s5025Zefa1"; // 替换为实际token

            // 构建请求头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", token);

            // 构建请求体
            String requestBody = buildRequestBody(text);

            // 发送请求
            HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

            // 解析响应
            JsonNode root = objectMapper.readTree(response.getBody());

            return root.path("choices")
                                      .path(0)
                                      .path("message")
                                      .path("content")
                                      .asText();
        } catch (Exception e) {
            throw new BusinessException(500, "翻译失败: " + e.getMessage());
        }
    }

    private String buildRequestBody(String text) throws JsonProcessingException {
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("assistant_id", "Tp2Wf3EASOyh");
        requestMap.put("user_id", "AURORA");
        requestMap.put("stream", false);

        List<Map<String, Object>> messages = new ArrayList<>();
        Map<String, Object> message = new HashMap<>();
        message.put("role", "user");

        List<Map<String, String>> content = new ArrayList<>();
        content.add(Map.of("type", "text", "text", text));

        message.put("content", content);
        messages.add(message);
        requestMap.put("messages", messages);

        return objectMapper.writeValueAsString(requestMap);
    }
}
