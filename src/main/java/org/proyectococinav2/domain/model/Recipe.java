package org.proyectococinav2.domain.model;

import java.time.LocalDate;

public class Recipe {
    private long id;
    private String name;
    private String instructions;
    private LocalDate date;

    public Recipe() {}

    public Recipe(long id, String name, String instructions, LocalDate date) {
        this.id = id;
        this.name = name;
        this.instructions = instructions;
        this.date = date;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
