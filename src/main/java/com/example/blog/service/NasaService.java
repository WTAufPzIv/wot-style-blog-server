package com.example.blog.service;

import com.example.blog.common.exception.BusinessException;
import com.example.blog.entity.NasaApod;
import com.example.blog.service.TransService;
import com.example.blog.model.dto.ResponseResult;
import com.example.blog.repository.NasaApodRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.time.LocalDate;
import java.util.Optional;

@Service
public class NasaService {
    private final RestTemplate restTemplate;
    private final NasaApodRepository repository;
    private final ObjectMapper objectMapper;
    private final TransService transService;

    @Value("${nasa.api.key}")
    private String apiKey;

    public NasaService(RestTemplateBuilder builder, NasaApodRepository repository, ObjectMapper objectMapper,  TransService transService) {
        this.restTemplate = builder.build();
        this.repository = repository;
        this.objectMapper = objectMapper;
        this.transService = transService;
    }

    public ResponseResult<String> getApodData() {
        LocalDate today = LocalDate.now();
        Optional<String> optionalSavedJson = repository.findByUpdateTime(today).map(NasaApod::getRawJson);
        if (optionalSavedJson.isEmpty()) {
            String newJson = fetchAndSaveFromApi(today);
            return ResponseResult.success(newJson);
        } else {
            return ResponseResult.success(optionalSavedJson.get());
        }
    }

    private String fetchAndSaveFromApi(LocalDate date) {
        try {
            String url = "https://api.nasa.gov/planetary/apod?api_key=" + apiKey;
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            JsonNode rootNode = objectMapper.readTree(response.getBody());
            String RawText = rootNode.path("explanation").asText();
            JsonNode optionalUrlNode = rootNode.path("url");
            if (optionalUrlNode.isEmpty()) {
                return fetchAndSaveFromApiByRandom(date);
            } else {
                String TransedText = transService.doTrans(RawText);
                ObjectNode modifiedNode = (ObjectNode) rootNode;
                modifiedNode.put("transedText", TransedText);
                String modifiedJson = objectMapper.writeValueAsString(modifiedNode);
                NasaApod entity = new NasaApod();
                entity.setDate(date);
                entity.setRawJson(modifiedJson);
                repository.save(entity);
                return modifiedJson;
            }
        } catch (Exception e) {
            throw new BusinessException(500, "NASA数据获取失败");
        }
    }

    private String fetchAndSaveFromApiByRandom(LocalDate date) {
        try {
            String url = "https://api.nasa.gov/planetary/apod?count=1&api_key=" + apiKey;
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            NasaApod entity = new NasaApod();
            JsonNode rootNode = objectMapper.readTree(response.getBody());
            String RawText = rootNode.path(0).path("explanation").asText();
            String TransedText = transService.doTrans(RawText);
            ObjectNode modifiedNode = (ObjectNode) rootNode.path(0);
            modifiedNode.put("transedText", TransedText);
            String modifiedJson = objectMapper.writeValueAsString(modifiedNode);
            entity.setDate(date);
            entity.setRawJson(modifiedJson);
            repository.save(entity);
            return modifiedJson;
        } catch (Exception e) {
            throw new BusinessException(500, "NASA数据获取失败");
        }
    }
}