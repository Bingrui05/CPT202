package cpt2021.test.service;

import cpt2021.test.entity.ExpertiseCategory;
import cpt2021.test.repository.ExpertiseCategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExpertiseCategoryService {

    private final ExpertiseCategoryRepository repository;

    public ExpertiseCategoryService(ExpertiseCategoryRepository repository) {
        this.repository = repository;
    }

    public List<ExpertiseCategory> findAll() {
        return repository.findAll();
    }

    public ExpertiseCategory save(ExpertiseCategory category) {
        return repository.save(category);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
