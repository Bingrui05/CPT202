package cpt2021.test.repository;

import cpt2021.test.entity.Specialist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SpecialistRepository extends JpaRepository<Specialist, Long> {

	@Query("SELECT s FROM Specialist s JOIN FETCH s.expertiseCategory JOIN FETCH s.level")
	List<Specialist> findAllFetched();

	@Query("SELECT s FROM Specialist s JOIN FETCH s.expertiseCategory JOIN FETCH s.level WHERE s.id = :id")
	Optional<Specialist> findByIdFetched(@Param("id") Long id);
}
