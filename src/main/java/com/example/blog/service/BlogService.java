package com.example.blog.service;

import com.example.blog.common.exception.BusinessException;
import com.example.blog.common.utils.PaginationUtils;
import com.example.blog.entity.Blog;
import com.example.blog.model.dto.ResponseResult;
import com.example.blog.model.vo.blog.BlogAddVO;
import com.example.blog.model.vo.blog.BlogDelVO;
import com.example.blog.model.vo.blog.BlogListGetVO;
import com.example.blog.model.vo.blog.BlogPutVO;
import com.example.blog.repository.BlogRepository;
import com.example.blog.mapper.BlogMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class BlogService {
    private static final Logger log = LoggerFactory.getLogger(BlogService.class);
    private final BlogRepository blogRepository;
    private final BlogMapper blogMapper;

    public BlogService(BlogRepository blogRepository, BlogMapper blogMapper) {
        this.blogRepository = blogRepository;
        this.blogMapper = blogMapper;
    }

    //================= 增 =================
    public ResponseResult<Blog> createBlog(BlogAddVO blogAdd) {
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
    public ResponseResult<Boolean> deleteBlog(BlogDelVO blogDel) {
        Long id = blogDel.getId();
        if (!blogRepository.existsById(id)) {
            throw new BusinessException(404, "博客不存在");
        }
        blogRepository.deleteById(id);
        return ResponseResult.success(true);
    }

    //================= 改 =================
    public ResponseResult<Blog> updateBlog(BlogPutVO blogPut) {
        Long id = blogPut.getId();
        Blog putedBlog = blogRepository.findById(id)
                .map(existingBlog -> {
                    // 仅更新允许修改的字段
                    existingBlog.setTitle(blogPut.getTitle());
                    existingBlog.setCategory(blogPut.getCategory());
                    existingBlog.setMiniDesc(blogPut.getMiniDesc());
                    existingBlog.setHeadImage(blogPut.getHeadImage());
                    existingBlog.setCreateTime(blogPut.getCreateTime());
                    existingBlog.setMdUrl(blogPut.getMdUrl());
                    return blogRepository.save(existingBlog);
                })
                .orElseThrow(() -> new BusinessException(404, "博客不存在"));
        return ResponseResult.success(putedBlog);
    }

    // ================ 分页查询 =================
    public ResponseResult<Map<String, Object>> getBlogList(BlogListGetVO listPageParams) {
        Pageable pageable = PaginationUtils.buildPageable(
                listPageParams.getPageIndex(),
                listPageParams.getPageSize(),
                Sort.by(Sort.Direction.DESC, "create_time")
        );

        // 使用PageHelper分页（注意参数顺序：pageNum从1开始）
        try (Page<Blog> page = PageHelper.startPage(  // 添加try-with-resources
                listPageParams.getPageIndex(),
                listPageParams.getPageSize(),
                true)) {  // 添加第三个参数启用count查询

            log.info(listPageParams.getKeywords());
            log.info(listPageParams.getCategory());

            blogMapper.findBlogsByConditions(
                    listPageParams.getKeywords(),
                    listPageParams.getCategory(),
                    listPageParams.getCreateTime()
            );

            return ResponseResult.success(PaginationUtils.buildPageResult(new PageImpl<>(
                    page.getResult(),
                    PageRequest.of(page.getPageNum() - 1, page.getPageSize(), pageable.getSort()),
                    page.getTotal()
            )));
        } // 自动关闭Page资源
    }

    // ================ 查询所有分类 =================
    public ResponseResult<List<String>> getAllCategories() {
        List<String> categories = blogMapper.findAllCategories();
        return ResponseResult.success(categories);
    }
}
