package com.cpt202.consultationbooking.repository;

import com.cpt202.consultationbooking.entity.Specialist;
import com.cpt202.consultationbooking.enums.SpecialistStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpecialistRepository extends JpaRepository<Specialist, Long> {
    
    List<Specialist> findByCategory_CategoryIdAndLevel_LevelId(Long categoryId, Long levelId);
    
    List<Specialist> findByCategory_CategoryIdAndLevel_LevelIdAndStatus(
            Long categoryId, Long levelId, SpecialistStatus status);
    
    List<Specialist> findByStatus(SpecialistStatus status);
}
