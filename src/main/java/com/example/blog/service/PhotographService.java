package com.example.blog.service;

import com.example.blog.common.exception.BusinessException;
import com.example.blog.entity.Blog;
import com.example.blog.entity.Photograph;
import com.example.blog.model.dto.ResponseResult;
import com.example.blog.model.vo.photograph.PhotoAddVO;
import com.example.blog.model.vo.photograph.PhotoDelVO;
import com.example.blog.model.vo.photograph.PhotoPutVO;
import com.example.blog.repository.PhotographRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PhotographService {
    PhotographRepository photographRepository;

    public PhotographService(PhotographRepository photographRepository) {
        this.photographRepository = photographRepository;
    }

    public ResponseResult<Photograph> createPhoto(PhotoAddVO photoAdd) {
        Photograph photograph = new Photograph();
        photograph.setTitle(photoAdd.getTitle());
        photograph.setCreateTime(photoAdd.getCreateTime());
        photograph.setContent(photoAdd.getContent());
        photograph.setImages(photoAdd.getImages());
        Photograph savedPhotograph = photographRepository.save(photograph);
        return ResponseResult.success(savedPhotograph);
    }

    public ResponseResult<Boolean> deletePhoto(PhotoDelVO photoDel) {
        Long id = photoDel.getId();
        if (!photographRepository.existsById(id)) {
            throw new BusinessException(404, "记录不存在");
        }
        photographRepository.deleteById(photoDel.getId());
        return ResponseResult.success(true);
    }

    public ResponseResult<Photograph> updatePhoto(PhotoPutVO photoPut) {
        Long id = photoPut.getId();
        Photograph putedBlog = photographRepository.findById(id)
                .map(existingPhoto -> {
                    // 仅更新允许修改的字段
                    existingPhoto.setTitle(photoPut.getTitle());
                    existingPhoto.setCreateTime(photoPut.getCreateTime());
                    existingPhoto.setContent(photoPut.getContent());
                    existingPhoto.setImages(photoPut.getImages());
                    return photographRepository.save(existingPhoto);
                })
                .orElseThrow(() -> new BusinessException(404, "记录不存在"));
        return ResponseResult.success(putedBlog);
    }

    public ResponseResult<Photograph> getPhotoById(Long id) {
        Photograph photograph = photographRepository.findById(id)
                .orElseThrow(() -> new BusinessException(404, "记录不存在"));
        return ResponseResult.success(photograph);
    }

    public ResponseResult<List<Photograph>> getPhotoList() {
        return ResponseResult.success(photographRepository.findAll());
    }
}
