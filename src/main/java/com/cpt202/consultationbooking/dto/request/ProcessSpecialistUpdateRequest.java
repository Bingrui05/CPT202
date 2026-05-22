package com.cpt202.consultationbooking.dto.request;

import jakarta.validation.constraints.Size;

public class ProcessSpecialistUpdateRequest {

    @Size(max = 1000, message = "Manager comment must not exceed 1000 characters")
    private String managerComment;

    public ProcessSpecialistUpdateRequest() {}

    public ProcessSpecialistUpdateRequest(String managerComment) {
        this.managerComment = managerComment;
    }

    public String getManagerComment() { return managerComment; }
    public void setManagerComment(String managerComment) { this.managerComment = managerComment; }
}
