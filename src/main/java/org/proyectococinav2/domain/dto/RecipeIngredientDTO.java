package org.proyectococinav2.domain.dto;

import org.proyectococinav2.domain.model.MeasurementUnit;

public class RecipeIngredientDTO {
    private Long recipeId;
    private Long ingredientId;
    private double servingPerPerson;
    private MeasurementUnit unit;
    private IngredientDTO ingredient;

    public RecipeIngredientDTO() {}

    public RecipeIngredientDTO(Long recipeId, Long ingredientId, double servingPerPerson, MeasurementUnit unit) {
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
    public IngredientDTO getIngredient() {
        return ingredient;
    }
    public void setIngredient(IngredientDTO ingredient) {
        this.ingredient = ingredient;
    }

    public MeasurementUnit getUnit() {
        return unit;
    }

    public void setUnit(MeasurementUnit unit) {
        this.unit = unit;
    }
}