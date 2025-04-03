package com.example.blog.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "blog")
public class Blog {
    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Setter
    @Column(unique = true)
    private LocalDate updateTime;

    @Getter
    @Setter
    private String title;

    @Getter
    @Setter
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") // 匹配前端传入格式
    private LocalDateTime createTime;

    @Getter
    @Setter
    private String category;

    @Getter
    @Setter
    private String headImage;

    @Getter
    @Setter
    @Column(columnDefinition = "TEXT")
    private String miniDesc;

    @Getter
    @Setter
    private String mdUrl;
}