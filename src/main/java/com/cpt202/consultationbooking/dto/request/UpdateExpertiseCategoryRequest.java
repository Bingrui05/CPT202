package com.cpt202.consultationbooking.dto.request;

import jakarta.validation.constraints.NotBlank;

public class UpdateExpertiseCategoryRequest {

    @NotBlank(message = "Category name is required")
    private String name;

    private String status;

    public UpdateExpertiseCategoryRequest() {}

    public UpdateExpertiseCategoryRequest(String name, String status) {
        this.name = name;
        this.status = status;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
