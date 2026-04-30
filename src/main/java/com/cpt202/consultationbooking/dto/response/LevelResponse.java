package com.cpt202.consultationbooking.dto.response;

public class LevelResponse {

    private Long levelId;
    private String name;

    public LevelResponse() {}

    public LevelResponse(Long levelId, String name) {
        this.levelId = levelId;
        this.name = name;
    }

    public Long getLevelId() { return levelId; }
    public void setLevelId(Long levelId) { this.levelId = levelId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
