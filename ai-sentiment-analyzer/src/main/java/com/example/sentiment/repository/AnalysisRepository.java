package com.example.sentiment.repository;

import com.example.sentiment.entity.Analysis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA Repository for managing Analysis entities in H2 database.
 */
@Repository
public interface AnalysisRepository extends JpaRepository<Analysis, Long> {

    /**
     * Retrieves all analyses ordered by newest first.
     */
    List<Analysis> findAllByOrderByCreatedAtDesc();
}
