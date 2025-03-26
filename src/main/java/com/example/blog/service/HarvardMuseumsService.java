package com.example.blog.service;

import com.example.blog.common.exception.BusinessException;
import com.example.blog.entity.HarvardMuseums;
import com.example.blog.model.dto.ResponseResult;
import com.example.blog.repository.HarvardMuseumsRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
public class HarvardMuseumsService {
    private final RestTemplate restTemplate;
    private final HarvardMuseumsRepository repository;
    private final ObjectMapper objectMapper;
    private final TransService transService;


    public HarvardMuseumsService(RestTemplateBuilder builder, HarvardMuseumsRepository repository, ObjectMapper objectMapper, TransService transService) {
        this.restTemplate = builder.build();
        this.repository = repository;
        this.objectMapper = objectMapper;
        this.transService = transService;
    }
    public ResponseResult<String> getHmDailly() {
        LocalDate today = LocalDate.now();
        Optional<String> optionalSavedJson = repository.findByUpdateTime(today).map(HarvardMuseums::getRawJson);
        if (optionalSavedJson.isEmpty()) {
            String newJson = fetchAndSaveFromApi(today);
            return ResponseResult.success(newJson);
        } else {
            return ResponseResult.success(optionalSavedJson.get());
        }
    }

    private String fetchAndSaveFromApi(LocalDate date) {
        try {
            // api 地址
            String url = "https://api.harvardartmuseums.org/object?apikey=e1535e1d-e532-47c0-91c9-2b44d67122e1&culture=Chinese&hasImage=1&sort=random&sortorder=desc&size=1&page=1";
            // 发送 GET 请求，并先将请求体序列化为json字符串
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            // 将序列化后的json串解析为JsonNode
            JsonNode rootNode = objectMapper.readTree(response.getBody());
            // 指针指向要保存的数据实体
            JsonNode optionalRecordNode = rootNode.path("records").path(0);
            if (optionalRecordNode.isEmpty()) {
                // 如果为空，则抛出异常
                throw new BusinessException(500, "HarvardMusiums数据获取为空");
            } else {
                // 极端情况下有可能直接没有图片
                JsonNode imagesNode = optionalRecordNode.path("images");
                if (imagesNode.isMissingNode()       // 字段不存在
                        || imagesNode.isNull()           // 字段值为null
                        || (imagesNode.isArray() && imagesNode.isEmpty())) { // 空数组
                    throw new BusinessException(500, "HarvardMusiums图片信息缺失");
                }
                // 待翻译列表
                ArrayList<String> rawTextList = new ArrayList<>();
                rawTextList.add(Objects.equals(optionalRecordNode.path("title").asText(), "null") ? "" : optionalRecordNode.path("title").asText());
                rawTextList.add(Objects.equals(optionalRecordNode.path("period").asText(), "null") ? "" : optionalRecordNode.path("period").asText());
                rawTextList.add(Objects.equals(optionalRecordNode.path("provenance")
                        .asText(), "null") ? "" : optionalRecordNode.path("provenance").asText()
                        .replaceAll("\\s+", "")
                        .replaceAll("\\[", "")
                        .replaceAll("]", "")
                        .replaceAll("\\n+", "")
                        .replaceAll("\\r", ""));
                rawTextList.add(Objects.equals(optionalRecordNode.path("dated").asText(), "null") ? "" : optionalRecordNode.path("dated").asText());
                rawTextList.add(Objects.equals(optionalRecordNode.path("classification").asText(), "null") ? "" : optionalRecordNode.path("classification").asText());
                ObjectMapper mapper = new ObjectMapper();
                // 执行翻译
                String TransedTextListStr = transService.doTrans(mapper.writeValueAsString(rawTextList));
                log.info(mapper.writeValueAsString(rawTextList));
                log.info(TransedTextListStr);
                // 返回的数组字符串转换为数组
                ObjectNode modifiedNode = getModifiedNode(TransedTextListStr, (ObjectNode) optionalRecordNode);
                String modifiedJson = objectMapper.writeValueAsString(modifiedNode);
                HarvardMuseums entity = new HarvardMuseums();
                // 写入数据库实体
                entity.setUpdateTime(date);
                entity.setRawJson(modifiedJson);
                // 插入到数据库
                repository.save(entity);
                return modifiedJson;
            }
        } catch (Exception e) {
            log.error("HarvardMusiums数据获取失败", e);
            throw new BusinessException(500, "HarvardMusiums数据获取失败");
        }
    }

    private static ObjectNode getModifiedNode(String TransedTextListStr, ObjectNode optionalRecordNode) {
        String[] TransedTextList = TransedTextListStr.substring(1, TransedTextListStr.length() - 1).split(",");
        // 塞回源对象实体
        optionalRecordNode.put("transedTitle", TransedTextList[0]);
        optionalRecordNode.put("transedPeriod", TransedTextList[1]);
        optionalRecordNode.put("transedProvenance", TransedTextList[2]);
        optionalRecordNode.put("transedDated", TransedTextList[3]);
        optionalRecordNode.put("transedClassification", TransedTextList[4]);
        return optionalRecordNode;
    }
}
