package com.example.blog.entity;

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