package com.example.blog.repository;

import com.example.blog.entity.Blog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BlogRepository extends JpaRepository<Blog, Long> {
    // 联合模糊查询（单参数同时匹配 title/category/desc 字段）
    @Query("SELECT DISTINCT b FROM Blog b WHERE " +
            "b.title LIKE %:keyword% OR " +
            "b.category LIKE %:keyword% OR " +
            "b.miniDesc LIKE %:keyword%")
    List<Blog> searchAllFields(@Param("keyword") String keyword);

    List<Blog> findByCategory(String category);
}