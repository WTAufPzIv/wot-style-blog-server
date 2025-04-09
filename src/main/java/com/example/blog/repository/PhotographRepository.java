package com.example.blog.repository;

import com.example.blog.entity.Photograph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhotographRepository extends JpaRepository<Photograph, Long> {}
