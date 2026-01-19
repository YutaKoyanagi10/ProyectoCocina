package org.proyectococina.domain.dto;

public class MenuItemDTO {
    private Long id;
    private Long recipeId;
    private String recipeName;
    private String dayOfWeek;
    private String mealTime;

    public MenuItemDTO() {}

    public MenuItemDTO(Long id, Long recipeId, String recipeName, String dayOfWeek, String mealTime) {
        this.id = id;
        this.recipeId = recipeId;
        this.recipeName = recipeName;
        this.dayOfWeek = dayOfWeek;
        this.mealTime = mealTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(Long recipeId) {
        this.recipeId = recipeId;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public String getMealTime() {
        return mealTime;
    }

    public void setMealTime(String mealTime) {
        this.mealTime = mealTime;
    }

}