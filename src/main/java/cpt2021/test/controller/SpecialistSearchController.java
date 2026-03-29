package cpt2021.test.controller;

import cpt2021.test.entity.Specialist;
import cpt2021.test.service.SpecialistSearchService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/specialists")
public class SpecialistSearchController {

    private final SpecialistSearchService specialistSearchService;

    public SpecialistSearchController(SpecialistSearchService specialistSearchService) {
        this.specialistSearchService = specialistSearchService;
    }

    @GetMapping("/search")
    public List<Specialist> search(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String level,
            @RequestParam(required = false) String category
    ) {
        return specialistSearchService.search(name, status, level, category);
    }
}
