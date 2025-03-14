package com.example.blog.controller;

import com.example.blog.entity.ResponseResult;
import com.example.blog.service.NasaService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class NasaController {
    private final NasaService nasaService;

    public NasaController(NasaService nasaService) {
        this.nasaService = nasaService;
    }

    @GetMapping("/getNasaAPOD")
    public ResponseResult getApod() {
        return ResponseResult.success(nasaService.getApodData());
    }
}
