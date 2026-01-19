package org.proyectococina.domain.dto;

import java.util.List;

public class WeeklyMenuDTO {
    private Long id;
    private String startDate;
    private String name;
    private List<MenuItemDTO> items;
    private String insertedAt;
    private String updatedAt;

    public WeeklyMenuDTO() {}

    public WeeklyMenuDTO(Long id, String startDate, String name, List<MenuItemDTO> items, String insertedAt, String updatedAt) {
        this.id = id;
        this.startDate = startDate;
        this.name = name;
        this.items = items;
        this.insertedAt = insertedAt;
        this.updatedAt = updatedAt;
    }

    public WeeklyMenuDTO(Long id, String startDate, String name, List<MenuItemDTO> items) {
        this.id = id;
        this.startDate = startDate;
        this.name = name;
        this.items = items;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<MenuItemDTO> getItems() {
        return items;
    }

    public void setItems(List<MenuItemDTO> items) {
        this.items = items;
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