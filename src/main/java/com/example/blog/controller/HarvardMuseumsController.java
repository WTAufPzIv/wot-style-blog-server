package com.example.blog.controller;

import com.example.blog.model.dto.ResponseResult;
import com.example.blog.service.HarvardMuseumsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auroraWeb")
public class HarvardMuseumsController {
    private final HarvardMuseumsService harvardMusiumsService;

    public HarvardMuseumsController(HarvardMuseumsService harvardMusiumsService) {
        this.harvardMusiumsService = harvardMusiumsService;
    }

    @PostMapping("/getHarvardMusiumsDailly")
    public ResponseResult<String> getHmDailly() {
        return harvardMusiumsService.getHmDailly();
    }
}
