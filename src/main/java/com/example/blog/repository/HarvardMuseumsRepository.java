package com.example.blog.repository;

import com.example.blog.entity.HarvardMuseums;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface HarvardMuseumsRepository extends JpaRepository<HarvardMuseums, Long> {
    Optional<HarvardMuseums> findByUpdateTime(LocalDate date);
}
