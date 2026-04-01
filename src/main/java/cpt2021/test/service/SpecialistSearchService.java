package cpt2021.test.service;

import cpt2021.test.entity.Specialist;
import cpt2021.test.repository.SpecialistRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpecialistSearchService {

    private final SpecialistRepository specialistRepository;

    public SpecialistSearchService(SpecialistRepository specialistRepository) {
        this.specialistRepository = specialistRepository;
    }

    public List<Specialist> search(String name, String status, String level, String category) {
        return specialistRepository.searchSpecialists(
                emptyToNull(name),
                emptyToNull(status),
                emptyToNull(level),
                emptyToNull(category)
        );
    }

    private String emptyToNull(String value) {
        return (value == null || value.isBlank()) ? null : value;
    }
}