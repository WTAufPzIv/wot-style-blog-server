package com.example.blog.controller;

import com.example.blog.annotation.RequestBodyValid;
import com.example.blog.entity.Blog;
import com.example.blog.model.dto.ResponseResult;
import com.example.blog.model.vo.adminVO;
import com.example.blog.model.vo.blog.blogAddVO;
import com.example.blog.service.BlogService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auroraWeb")
public class BlogController {
    private final BlogService blogService;

    public BlogController(BlogService blogService) {
        this.blogService = blogService;
    }

    //================= 增 =================
    @PostMapping(value = "/blog/add", consumes = {MediaType.TEXT_PLAIN_VALUE})
    public ResponseResult<Blog> create(@RequestBodyValid(targetClass = blogAddVO.class, fields = {"title", "createTime", "category", "headImage", "miniDesc", "mdUrl"}) blogAddVO blogAdd) throws JsonProcessingException {
        return blogService.createBlog((Blog) blogAdd);
    }

    //================= 删 =================
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        blogService.deleteBlog(id);
        return ResponseEntity.noContent().build();
    }

    //================= 改 =================
    @PutMapping("/{id}")
    public ResponseEntity<Blog> update(
            @PathVariable Long id,
            @RequestBody Blog blog
    ) {
        return ResponseEntity.ok(blogService.updateBlog(id, blog));
    }

    //================= 查所有 =================
    @GetMapping("/{id}")
    public ResponseEntity<Blog> get(@PathVariable Long id) {
        return ResponseEntity.ok(blogService.getBlog(id));
    }

    //================= 模糊搜索 =================
    @GetMapping("/search")
    public ResponseEntity<List<Blog>> search(@RequestParam String keyword) {
        return ResponseEntity.ok(blogService.searchAllFields(keyword));
    }
}
