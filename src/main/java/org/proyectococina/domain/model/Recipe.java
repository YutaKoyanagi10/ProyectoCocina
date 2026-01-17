package org.proyectococina.domain.model;

import java.time.LocalDateTime;

public class Recipe {
    private Long id;
    private String name;
    private String instructions;
    private LocalDateTime insertedAt;
    private LocalDateTime updatedAt;

    public Recipe() {}

    public Recipe(Long id, String name, String instructions) {
        this.id = id;
        this.name = name;
        this.instructions = instructions;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public LocalDateTime getInsertedAt() {
        return insertedAt;
    }

    public void setInsertedAt(LocalDateTime insertedAt) {
        this.insertedAt = insertedAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
