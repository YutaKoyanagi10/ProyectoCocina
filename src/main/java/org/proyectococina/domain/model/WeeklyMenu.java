package org.proyectococina.domain.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class WeeklyMenu {
    private Long id;
    private LocalDate startDate;
    private String name;
    private LocalDateTime insertedAt;
    private LocalDateTime updatedAt;

    public WeeklyMenu() {}

    public WeeklyMenu(Long id, LocalDate startDate, String name) {
        this.id = id;
        this.startDate = startDate;
        this.name = name;
    }

    public Long getId() { return id; }
    public LocalDate getStartDate() { return startDate; }
    public String getName() { return name; }
    public LocalDateTime getInsertedAt() { return insertedAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setName(String name) { this.name = name; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public void setId(Long id) { this.id = id; }
    public void setInsertedAt(LocalDateTime insertedAt) { this.insertedAt = insertedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
} 