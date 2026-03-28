package cpt2021.test.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record SpecialistRequest(
		@NotBlank @Size(max = 128) String displayName,
		@Size(max = 255) String email,
		@Size(max = 32) String phone,
		@Size(max = 2000) String bio,
		@NotNull Long expertiseCategoryId,
		@NotNull Long levelId) {
}
