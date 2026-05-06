package com.cpt202.consultationbooking.repository;

import com.cpt202.consultationbooking.entity.Specialist;
import com.cpt202.consultationbooking.enums.SpecialistStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpecialistRepository extends JpaRepository<Specialist, Long> {
    
    List<Specialist> findByCategory_CategoryId(Long categoryId);
    
    List<Specialist> findByLevel_LevelId(Long levelId);
    
    List<Specialist> findByCategory_CategoryIdAndLevel_LevelId(Long categoryId, Long levelId);
    
    List<Specialist> findByCategory_CategoryIdAndStatus(Long categoryId, SpecialistStatus status);
    
    List<Specialist> findByLevel_LevelIdAndStatus(Long levelId, SpecialistStatus status);
    
    List<Specialist> findByCategory_CategoryIdAndLevel_LevelIdAndStatus(
            Long categoryId, Long levelId, SpecialistStatus status);
    
    List<Specialist> findByStatus(SpecialistStatus status);
    
    @Query("SELECT s FROM Specialist s WHERE " +
           "LOWER(s.user.username) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(s.user.email) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(s.category.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(s.level.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(s.information) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(CAST(s.status AS string)) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Specialist> searchByKeyword(@Param("keyword") String keyword);
    
    @Query("SELECT DISTINCT s FROM Specialist s WHERE " +
           "(LOWER(s.user.username) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(s.user.email) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(s.category.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(s.level.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(s.information) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(CAST(s.status AS string)) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
           "AND (:categoryId IS NULL OR s.category.categoryId = :categoryId) " +
           "AND (:levelId IS NULL OR s.level.levelId = :levelId) " +
           "AND (:status IS NULL OR s.status = :status) " +
           "AND (:onlyAvailable = false OR EXISTS " +
           "  (SELECT 1 FROM s.slotList sl WHERE sl.status = 'AVAILABLE'))")
    List<Specialist> searchByKeywordWithFilters(
            @Param("keyword") String keyword,
            @Param("categoryId") Long categoryId,
            @Param("levelId") Long levelId,
            @Param("status") SpecialistStatus status,
            @Param("onlyAvailable") boolean onlyAvailable);
    
    @Query("SELECT DISTINCT s FROM Specialist s WHERE " +
           "(:categoryId IS NULL OR s.category.categoryId = :categoryId) " +
           "AND (:levelId IS NULL OR s.level.levelId = :levelId) " +
           "AND (:status IS NULL OR s.status = :status) " +
           "AND (:onlyAvailable = false OR EXISTS " +
           "  (SELECT 1 FROM s.slotList sl WHERE sl.status = 'AVAILABLE'))")
    List<Specialist> searchByFiltersOnly(
            @Param("categoryId") Long categoryId,
            @Param("levelId") Long levelId,
            @Param("status") SpecialistStatus status,
            @Param("onlyAvailable") boolean onlyAvailable);
    
    @Query("SELECT DISTINCT s FROM Specialist s WHERE EXISTS " +
           "(SELECT 1 FROM s.slotList sl WHERE sl.status = 'AVAILABLE')")
    List<Specialist> findSpecialistsWithAvailableSlots();
}
