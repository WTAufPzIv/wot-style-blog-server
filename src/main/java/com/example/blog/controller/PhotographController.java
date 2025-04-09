package com.example.blog.controller;

import com.example.blog.annotation.LoginCheck;
import com.example.blog.entity.Photograph;
import com.example.blog.model.dto.ResponseResult;
import com.example.blog.model.vo.photograph.PhotoAddVO;
import com.example.blog.model.vo.photograph.PhotoDelVO;
import com.example.blog.model.vo.photograph.PhotoPutVO;
import com.example.blog.service.PhotographService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/auroraWeb")
public class PhotographController {
    PhotographService photographService;

    public PhotographController(PhotographService photographService) {
        this.photographService = photographService;
    }

    //================= 增 =================
    @PostMapping(value = "/photograph/add")
    @LoginCheck
    public ResponseResult<Photograph> create(
            @RequestBody
            @Valid
            PhotoAddVO photoAdd
    ) {
        return photographService.createPhoto(photoAdd);
    }

    //================= 删 =================
    @PostMapping("/photograph/delete")
    @LoginCheck
    public ResponseResult<Boolean> delete(
            @RequestBody
            @Valid
            PhotoDelVO photoDel
    ) {
        return photographService.deletePhoto(photoDel);
    }

    //================= 改 =================
    @PostMapping("/photograph/put")
    @LoginCheck
    public ResponseResult<Photograph> update(
            @RequestBody
            @Valid
            PhotoPutVO photoPut
    ) {
        return photographService.updatePhoto(photoPut);
    }

    @PostMapping(value = "/photograph/list")
    public ResponseResult<List<Photograph>> getList() {
        return photographService.getPhotoList();
    }

    @PostMapping(value = "/photograph/detail")
    public ResponseResult<Photograph> getPhotoById(
            @RequestBody
            @Valid
            Long id
    ) {
        return photographService.getPhotoById(id);
    }
}
