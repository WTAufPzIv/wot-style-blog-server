package com.example.blog.controller;

import com.example.blog.model.dto.ResponseResult;
import com.example.blog.service.NationGeographicService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auroraWeb")
public class NationalGeographicController {
    private final NationGeographicService nationGeographicService;

    public NationalGeographicController(NationGeographicService nationGeographicService) {
        this.nationGeographicService = nationGeographicService;
    }

    @PostMapping("/getNationalGeographicDailly")
    public ResponseResult<String> getNgDailly() {
        return nationGeographicService.getNgDailly();
    }
}
