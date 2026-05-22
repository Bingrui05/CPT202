package cpt2021.test.config;

import cpt2021.test.entity.ExpertiseCategory;
import cpt2021.test.entity.Level;
import cpt2021.test.repository.ExpertiseCategoryRepository;
import cpt2021.test.repository.LevelRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ReferenceDataInitializer {

	@Bean
	CommandLineRunner seedExpertiseAndLevels(
			ExpertiseCategoryRepository expertiseCategoryRepository, LevelRepository levelRepository) {
		return args -> {
			if (expertiseCategoryRepository.count() == 0) {
				expertiseCategoryRepository.save(new ExpertiseCategory("Software Engineering"));
				expertiseCategoryRepository.save(new ExpertiseCategory("Data Science"));
				expertiseCategoryRepository.save(new ExpertiseCategory("Product Design"));
			}
			if (levelRepository.count() == 0) {
				levelRepository.save(new Level("Junior"));
				levelRepository.save(new Level("Mid"));
				levelRepository.save(new Level("Senior"));
			}
		};
	}
}
