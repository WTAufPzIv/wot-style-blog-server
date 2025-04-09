package com.example.blog.entity;

import com.example.blog.common.utils.SnowflakeIdGenerator;
import com.example.blog.converter.StringListConverter;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Entity
@Table(name = "photograph")
public class Photograph {
    @Id
    @GeneratedValue(generator = "snowflake")
    @GenericGenerator(name = "snowflake", type = SnowflakeIdGenerator.class)
    private Long id;

    @Setter
    private String title;

    @Setter
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") // 匹配前端传入格式
    private LocalDateTime createTime;

    @Setter
    @Column(columnDefinition = "TEXT")
    private String content;

    @Setter
    @Column(columnDefinition = "TEXT")
    @Convert(converter = StringListConverter.class)
    private List<String> images;
}
