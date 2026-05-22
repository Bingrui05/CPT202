package com.cpt202.consultationbooking.repository;

import com.cpt202.consultationbooking.entity.ExpertiseCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExpertiseCategoryRepository extends JpaRepository<ExpertiseCategory, Long> {

    boolean existsByNameIgnoreCase(String name);

    Optional<ExpertiseCategory> findByNameIgnoreCase(String name);
}
