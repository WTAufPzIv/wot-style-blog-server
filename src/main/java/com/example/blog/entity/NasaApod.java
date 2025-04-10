package com.example.blog.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "nasa")
public class NasaApod {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Setter
    @Column(unique = true)
    private LocalDate updateTime;

    @Getter
    @Setter
    @Column(columnDefinition = "TEXT")
    private String rawJson;
}