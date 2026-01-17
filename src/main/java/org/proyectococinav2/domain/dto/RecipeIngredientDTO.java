package org.proyectococinav2.domain.dto;

import org.proyectococinav2.domain.model.MeasurementUnit;

public class RecipeIngredientDTO {
    private double quantity;
    private String ingredientName;
    private MeasurementUnit unit;

    public RecipeIngredientDTO() {}

    public RecipeIngredientDTO(String ingredientName, double quantity, MeasurementUnit unit) {
        this.ingredientName = ingredientName;
        this.quantity = quantity;
        this.unit = unit;
    }
    public String getIngredientName() {
        return ingredientName;
    }
    public void setIngredientName(String ingredientName) {
        this.ingredientName = ingredientName;
    }
    public double getQuantity() {
        return quantity;
    }
    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public MeasurementUnit getUnit() {
        return unit;
    }

    public void setUnit(MeasurementUnit unit) {
        this.unit = unit;
    }
}