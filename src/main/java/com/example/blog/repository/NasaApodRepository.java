package com.example.blog.repository;

import com.example.blog.entity.NasaApod;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface NasaApodRepository extends JpaRepository<NasaApod, Long> {
    Optional<NasaApod> findByUpdateTime(LocalDate date);
}