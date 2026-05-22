package cpt2021.test.controller;

import cpt2021.test.dto.SpecialistRequest;
import cpt2021.test.dto.SpecialistResponse;
import cpt2021.test.service.SpecialistService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/specialists")
public class SpecialistController {

	private final SpecialistService specialistService;

	public SpecialistController(SpecialistService specialistService) {
		this.specialistService = specialistService;
	}

	@GetMapping
	public List<SpecialistResponse> list() {
		return specialistService.listAll();
	}

	@GetMapping("/{id}")
	public SpecialistResponse get(@PathVariable Long id) {
		return specialistService.getById(id);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public SpecialistResponse create(@Valid @RequestBody SpecialistRequest request) {
		return specialistService.create(request);
	}

	@PutMapping("/{id}")
	public SpecialistResponse update(@PathVariable Long id, @Valid @RequestBody SpecialistRequest request) {
		return specialistService.update(id, request);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long id) {
		specialistService.delete(id);
	}
}
