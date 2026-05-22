package com.cpt202.consultationbooking.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "levels")
public class Level {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long levelId;

    @Column(nullable = false)
    private String name;

    public Level() {}

    public Level(Long levelId, String name) {
        this.levelId = levelId;
        this.name = name;
    }

    public Long getLevelId() { return levelId; }
    public void setLevelId(Long levelId) { this.levelId = levelId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public static LevelBuilder builder() { return new LevelBuilder(); }

    public static class LevelBuilder {
        private Long levelId;
        private String name;

        public LevelBuilder levelId(Long levelId) { this.levelId = levelId; return this; }
        public LevelBuilder name(String name) { this.name = name; return this; }
        public Level build() { return new Level(levelId, name); }
    }
}
