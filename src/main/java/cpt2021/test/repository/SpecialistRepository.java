package cpt2021.test.repository;

import cpt2021.test.entity.Specialist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SpecialistRepository extends JpaRepository<Specialist, Long> {

    @Query("""
        SELECT s FROM Specialist s
        WHERE (:name IS NULL OR LOWER(s.name) LIKE LOWER(CONCAT('%', :name, '%')))
          AND (:status IS NULL OR s.status = :status)
          AND (:level IS NULL OR s.level.name = :level)
          AND (:category IS NULL OR s.expertiseCategory.name = :category)
    """)
    List<Specialist> searchSpecialists(
            @Param("name") String name,
            @Param("status") String status,
            @Param("level") String level,
            @Param("category") String category
    );
}