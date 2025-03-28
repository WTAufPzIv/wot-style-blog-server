package com.example.blog.repository;

import com.example.blog.entity.NationalGeographic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface NationalGeographicRepository extends JpaRepository<NationalGeographic, Long> {
    Optional<NationalGeographic> findByUpdateTime(LocalDate date);
}
