package cpt2021.test.dto;

import java.time.Instant;

public record SpecialistResponse(
		Long id,
		String displayName,
		String email,
		String phone,
		String bio,
		Long expertiseCategoryId,
		String expertiseCategoryName,
		Long levelId,
		String levelName,
		Instant createdAt,
		Instant updatedAt) {
}
