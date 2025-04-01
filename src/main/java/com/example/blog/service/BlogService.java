package com.example.blog.service;

import com.example.blog.common.exception.BusinessException;
import com.example.blog.entity.Blog;
import com.example.blog.model.dto.ResponseResult;
import com.example.blog.model.vo.blog.blogAddVO;
import com.example.blog.repository.BlogRepository;
import org.springframework.stereotype.Service;

@Service
public class BlogService {
    private final BlogRepository blogRepository;

    public BlogService(BlogRepository blogRepository) {
        this.blogRepository = blogRepository;
    }

    //================= 增 =================
    public ResponseResult<Blog> createBlog(blogAddVO blogAdd) {
//        Blog savedBlog = blogRepository.save(blog);
        Blog blog = new Blog();
        blog.setTitle(blogAdd.getTitle());
        blog.setCategory(blogAdd.getCategory());
        blog.setMiniDesc(blogAdd.getMiniDesc());
        blog.setHeadImage(blogAdd.getHeadImage());
        blog.setMdUrl(blogAdd.getMdUrl());
        blog.setCreateTime(blogAdd.getCreateTime());

        Blog savedBlog = blogRepository.save(blog);
        return ResponseResult.success(savedBlog);
    }

    //================= 删 =================
    public ResponseResult<Boolean> deleteBlog(Long id) {
        if (!blogRepository.existsById(id)) {
            throw new BusinessException(404, "博客不存在");
        }
        blogRepository.deleteById(id);
        return ResponseResult.success(true);
    }

    //================= 改 =================
    public ResponseResult<Blog> updateBlog(Long id, Blog newBlog) {
        Blog putedBlog = blogRepository.findById(id)
                .map(existingBlog -> {
                    // 仅更新允许修改的字段
                    existingBlog.setTitle(newBlog.getTitle());
                    existingBlog.setCategory(newBlog.getCategory());
                    existingBlog.setMiniDesc(newBlog.getMiniDesc());
                    existingBlog.setHeadImage(newBlog.getHeadImage());
                    existingBlog.setCreateTime(newBlog.getCreateTime());
                    existingBlog.setMdUrl(newBlog.getMdUrl());
                    return blogRepository.save(existingBlog);
                })
                .orElseThrow(() -> new BusinessException(404, "博客不存在"));
        return ResponseResult.success(putedBlog);
    }
}
