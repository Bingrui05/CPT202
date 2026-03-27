package cpt2021.test.controller;

import cpt2021.test.entity.ExpertiseCategory;
import cpt2021.test.service.ExpertiseCategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ExpertiseCategoryController {

    private final ExpertiseCategoryService service;

    public ExpertiseCategoryController(ExpertiseCategoryService service) {
        this.service = service;
    }

    @GetMapping("/categories")
    public String listCategories(Model model) {
        model.addAttribute("categories", service.findAll());
        return "categories";
    }

    @GetMapping("/categories/new")
    public String showCategoryForm(Model model) {
        model.addAttribute("category", new ExpertiseCategory());
        return "category-form";
    }

    @PostMapping("/categories")
    public String saveCategory(@ModelAttribute ExpertiseCategory category) {
        service.save(category);
        return "redirect:/categories";
    }

    @GetMapping("/categories/delete/{id}")
    public String deleteCategory(@PathVariable Long id) {
        service.deleteById(id);
        return "redirect:/categories";
    }
}
