package cpt2021.test.repository;

import cpt2021.test.entity.ExpertiseCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpertiseCategoryRepository extends JpaRepository<ExpertiseCategory, Long> {
}
