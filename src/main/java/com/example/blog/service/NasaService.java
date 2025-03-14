package com.example.blog.service;

import com.example.blog.entity.NasaApod;
import com.example.blog.repository.NasaApodRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;

@Service
public class NasaService {
    private final RestTemplate restTemplate;
    private final NasaApodRepository repository;

    @Value("${nasa.api.key}")
    private String apiKey;

    public NasaService(RestTemplateBuilder builder, NasaApodRepository repository) {
        this.restTemplate = builder.build();
        this.repository = repository;
    }

    public String getApodData() {
        LocalDate today = LocalDate.now();
        return repository.findByUpdateTime(today)
                .map(NasaApod::getRawJson)
                .orElseGet(() -> fetchAndSaveFromApi(today));
    }

    private String fetchAndSaveFromApi(LocalDate date) {
        String url = "https://api.nasa.gov/planetary/apod?api_key=" + apiKey;
        String response = restTemplate.getForObject(url, String.class);

        NasaApod entity = new NasaApod();
        entity.setDate(date);
        entity.setRawJson(response);
        repository.save(entity);

        return response;
    }
}