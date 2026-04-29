package com.cpt202.consultationbooking.repository;

import com.cpt202.consultationbooking.entity.ExpertiseCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpertiseCategoryRepository extends JpaRepository<ExpertiseCategory, Long> {
}
