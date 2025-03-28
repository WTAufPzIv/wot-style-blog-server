package com.example.blog.service;

import com.example.blog.common.exception.BusinessException;
import com.example.blog.entity.NationalGeographic;
import com.example.blog.model.dto.ResponseResult;
import com.example.blog.repository.NationalGeographicRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class NationGeographicService {
    private final RestTemplate restTemplate;
    private final NationalGeographicRepository repository;
    private final ObjectMapper objectMapper;


    public NationGeographicService(RestTemplateBuilder builder, NationalGeographicRepository repository, ObjectMapper objectMapper) {
        this.restTemplate = builder.build();
        this.repository = repository;
        this.objectMapper = objectMapper;
    }

    public ResponseResult<String> getNgDailly() {
        LocalDate today = LocalDate.now();
        Optional<String> optionalSavedJson = repository.findByUpdateTime(today).map(NationalGeographic::getRawJson);
        if (optionalSavedJson.isEmpty()) {
            String newJson = fetchAndSaveFromApi(today);
            return ResponseResult.success(newJson);
        } else {
            return ResponseResult.success(optionalSavedJson.get());
        }
    }

    private String fetchAndSaveFromApi(LocalDate date) {
        try {
            // 在 fetchAndSaveFromApi 方法中构建请求体
            Map<String, Object> requestBody = getRequestBody();
            String url = "https://www.ngchina.com.cn/api/ex/cms/news/list";
            // 发送 GET 请求，并先将请求体序列化为json字符串
            ResponseEntity<String> response = restTemplate.postForEntity(url, requestBody, String.class);
            // 将序列化后的json串解析为JsonNode
            JsonNode rootNode = objectMapper.readTree(response.getBody());
            // 指针指向要保存的数据实体
            JsonNode optionalRecordNode = rootNode.path("rows");
            if (optionalRecordNode.isMissingNode() || optionalRecordNode.isNull() || (optionalRecordNode.isArray() && optionalRecordNode.isEmpty())) {
                // 如果为空，则抛出异常
                throw new BusinessException(500, "NationalGeographic数据获取为空");
            } else {
                NationalGeographic entity = new NationalGeographic();
                // 改用 ObjectMapper 序列化
                String ImageListJson = objectMapper.writeValueAsString(optionalRecordNode);
                log.info(optionalRecordNode.toString());
                // 写入数据库实体
                entity.setUpdateTime(date);
                entity.setRawJson(ImageListJson);
                // 插入到数据库
                repository.save(entity);
                return ImageListJson;
            }
        } catch (Exception e) {
            log.error("NationalGeographic数据获取失败", e);
            throw new BusinessException(500, "NationalGeographic数据获取失败");
        }
    }

    private static Map<String, Object> getRequestBody() {
        Map<String, Object> requestBody = new HashMap<>();
        // 嵌套 cmsNews 对象
        Map<String, Object> cmsNews = new HashMap<>();
        cmsNews.put("categoryId", 18);
        cmsNews.put("isWebsite", "Y");
        // 嵌套 pageDomain 对象
        Map<String, Integer> pageDomain = new HashMap<>();
        pageDomain.put("pageNum", 1);
        pageDomain.put("pageSize", 5);
        // 组合完整请求体
        requestBody.put("cmsNews", cmsNews);
        requestBody.put("pageDomain", pageDomain);
        return requestBody;
    }
}
