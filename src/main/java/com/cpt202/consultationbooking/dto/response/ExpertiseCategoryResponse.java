package com.cpt202.consultationbooking.dto.response;

public class ExpertiseCategoryResponse {

    private Long categoryId;
    private String name;
    private String status;

    public ExpertiseCategoryResponse() {}

    public ExpertiseCategoryResponse(Long categoryId, String name, String status) {
        this.categoryId = categoryId;
        this.name = name;
        this.status = status;
    }

    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
