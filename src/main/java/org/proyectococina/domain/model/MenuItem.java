package org.proyectococina.domain.model;

import java.time.LocalDateTime;

public class MenuItem {
    private Long id;
    private Long menuId;
    private Long recipeId;
    private String dayOfWeek;
    private String mealTime;
    private LocalDateTime insertedAt;
    private LocalDateTime updatedAt;

    public MenuItem() {}

    public MenuItem(Long id, Long menuId, Long recipeId, String dayOfWeek, String mealTime) {
        this.id = id;
        this.menuId = menuId;
        this.recipeId = recipeId;
        this.dayOfWeek = dayOfWeek;
        this.mealTime = mealTime;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getMenuId() { return menuId; }
    public void setMenuId(Long menuId) { this.menuId = menuId; }
    public Long getRecipeId() { return recipeId; }
    public void setRecipeId(Long recipeId) { this.recipeId = recipeId; }
    public String getDayOfWeek() { return dayOfWeek; }
    public void setDayOfWeek(String dayOfWeek) { this.dayOfWeek = dayOfWeek; }
    public String getMealTime() { return mealTime; }
    public void setMealTime(String mealTime) { this.mealTime = mealTime; }
    public LocalDateTime getInsertedAt() { return insertedAt; }
    public void setInsertedAt(LocalDateTime insertedAt) { this.insertedAt = insertedAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}