package com.cpt202.consultationbooking.dto.request;

public class ProcessRequestRequest {

    private String managerComment;

    public ProcessRequestRequest() {}

    public ProcessRequestRequest(String managerComment) {
        this.managerComment = managerComment;
    }

    public String getManagerComment() { return managerComment; }
    public void setManagerComment(String managerComment) { this.managerComment = managerComment; }
}
