package com.example.blog.controller;

import com.example.blog.annotation.DecryptRequestBody;
import com.example.blog.annotation.LoginCheck;
import com.example.blog.entity.Blog;
import com.example.blog.model.dto.ResponseResult;
import com.example.blog.model.vo.blog.BlogAddVO;
import com.example.blog.service.BlogService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/auroraWeb")
public class BlogController {
    private final BlogService blogService;

    public BlogController(BlogService blogService) {
        this.blogService = blogService;
    }

    //================= 增 =================
    @PostMapping(value = "/blog/add")
//    @LoginCheck
    public ResponseResult<Blog> create(
            @DecryptRequestBody
            @Valid
            BlogAddVO blogAdd
    ) {
        return blogService.createBlog(blogAdd);
    }

//    //================= 删 =================
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> delete(@PathVariable Long id) {
//        blogService.deleteBlog(id);
//        return ResponseEntity.noContent().build();
//    }
//
//    //================= 改 =================
//    @PutMapping("/{id}")
//    public ResponseEntity<Blog> update(
//            @PathVariable Long id,
//            @RequestBody Blog blog
//    ) {
//        return ResponseEntity.ok(blogService.updateBlog(id, blog));
//    }
//
//    //================= 查所有 =================
//    @GetMapping("/{id}")
//    public ResponseEntity<Blog> get(@PathVariable Long id) {
//        return ResponseEntity.ok(blogService.getBlog(id));
//    }
//
//    //================= 模糊搜索 =================
//    @GetMapping("/search")
//    public ResponseEntity<List<Blog>> search(@RequestParam String keyword) {
//        return ResponseEntity.ok(blogService.searchAllFields(keyword));
//    }
}
