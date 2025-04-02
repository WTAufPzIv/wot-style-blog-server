package com.example.blog.controller;

import com.example.blog.annotation.RequestBodyValid;
import com.example.blog.model.dto.ResponseResult;
import com.example.blog.model.vo.TransVO;
import com.example.blog.service.TransService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auroraWeb")
public class TransController {
    private final TransService transService;

    public TransController(TransService transService) {
        this.transService = transService;
    }

    @PostMapping(value = "/transText", consumes = {MediaType.TEXT_PLAIN_VALUE})
    public ResponseResult<String> transText(@RequestBodyValid(targetClass = TransVO.class, fields = {"text"}) TransVO trans) throws JsonProcessingException {
        return transService.handleTrans(trans.getText());
    }
}