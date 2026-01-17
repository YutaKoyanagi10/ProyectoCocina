package org.proyectococina.domain.model;

import java.time.LocalDateTime;

public class Ingredient {
    private Long id;
    private String name;
    private Long supplierId;
    private LocalDateTime insertedAt;
    private LocalDateTime updatedAt;

    public Ingredient() {}

    public Ingredient(Long id, String name, Long supplierId) {
        this.id = id;
        this.name = name;
        this.supplierId = supplierId;
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
    public Long getSupplierId() {
        return supplierId;
    }
    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
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