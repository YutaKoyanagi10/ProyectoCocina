package org.proyectococina.domain.model;

import java.time.LocalDateTime;

public class RecipeIngredient {
    private Long recipeId;
    private Long ingredientId;
    private double servingPerPerson;
    private MeasurementUnit unit;
    private LocalDateTime insertedAt;
    private LocalDateTime updatedAt;

    public RecipeIngredient() {}

    public RecipeIngredient(Long recipeId, Long ingredientId, double servingPerPerson, MeasurementUnit unit) {
        this.recipeId = recipeId;
        this.ingredientId = ingredientId;
        this.servingPerPerson = servingPerPerson;
        this.unit = unit;
    }
    public Long getRecipeId() {
        return recipeId;
    }
    public void setRecipeId(Long recipeId) {
        this.recipeId = recipeId;
    }
    public Long getIngredientId() {
        return ingredientId;
    }
    public void setIngredientId(Long ingredientId) {
        this.ingredientId = ingredientId;
    }
    public double getServingPerPerson() {
        return servingPerPerson;
    }
    public void setServingPerPerson(double servingPerPerson) {
        this.servingPerPerson = servingPerPerson;
    }
    public MeasurementUnit getUnit() {
        return unit;
    }
    public void setUnit(MeasurementUnit unit) {
        this.unit = unit;
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
