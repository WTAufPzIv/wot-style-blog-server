package com.example.blog.mapper;

import com.example.blog.entity.Blog;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Mapper
public interface BlogMapper {
    List<Blog> findBlogsByConditions(
            @Param("keywords") String keywords,
            @Param("category") String category,
            @Param("createTime") String createTime
    );

    List<String> findAllCategories();
}
