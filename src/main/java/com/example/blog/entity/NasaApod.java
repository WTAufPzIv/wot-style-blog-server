package com.example.blog.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDate;
import java.util.Date;

@Entity
public class NasaApod {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private LocalDate updateTime;

    @Column(columnDefinition = "TEXT")
    private String rawJson;

    public void setDate(LocalDate updateTime) {
        this.updateTime = updateTime;
    }

    public void setRawJson(String jsonstr) {
        this.rawJson = jsonstr;
    }

    public String getRawJson() {
        return this.rawJson;
    }
}