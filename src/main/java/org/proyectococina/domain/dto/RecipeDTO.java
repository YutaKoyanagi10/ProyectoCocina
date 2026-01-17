package org.proyectococina.domain.dto;

import java.util.ArrayList;
import java.util.List;

public class RecipeDTO {
    private Long id;
    private String name;
    private String instructions;
    private List<RecipeIngredientDTO> ingredients = new ArrayList<>();
    private String insertedAt;
    private String updatedAt;

    public RecipeDTO() {}

    public RecipeDTO(Long id, String name, String instructions,List<RecipeIngredientDTO> ingredients, String insertedAt, String updatedAt) {
        this.id = id;
        this.name = name;
        this.instructions = instructions;
        this.ingredients = ingredients;
        this.insertedAt = insertedAt;
        this.updatedAt = updatedAt;
    }

    public RecipeDTO(Long id, String name, String instructions, List<RecipeIngredientDTO> ingredients) {
        this.id = id;
        this.name = name;
        this.instructions = instructions;
        this.ingredients = ingredients;
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

    public List<RecipeIngredientDTO> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<RecipeIngredientDTO> ingredients) {
        this.ingredients = ingredients;
    }

    public void addIngredient(RecipeIngredientDTO ingredient) {
        this.ingredients.add(ingredient);
    }

    public String getInsertedAt() {
        return insertedAt;
    }

    public void setInsertedAt(String insertedAt) {
        this.insertedAt = insertedAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}