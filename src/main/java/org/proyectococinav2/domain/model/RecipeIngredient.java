package org.proyectococinav2.domain.model;

public class RecipeIngredient {
    private long recipeId;
    private long ingredientId;
    private double servingPerPerson;
    private MeasurementUnit unit;

    public RecipeIngredient() {}

    public RecipeIngredient(long recipeId, long ingredientId, double servingPerPerson, MeasurementUnit unit) {
        this.recipeId = recipeId;
        this.ingredientId = ingredientId;
        this.servingPerPerson = servingPerPerson;
        this.unit = unit;
    }
    public long getRecipeId() {
        return recipeId;
    }
    public void setRecipeId(long recipeId) {
        this.recipeId = recipeId;
    }
    public long getIngredientId() {
        return ingredientId;
    }
    public void setIngredientId(long ingredientId) {
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
}
