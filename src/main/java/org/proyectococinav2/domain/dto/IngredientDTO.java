package org.proyectococinav2.domain.dto;

public class IngredientDTO {
    private Long id;
    private String name;
    private String supplierName;
    private String insertedAt; 
    private String updatedAt;

    public IngredientDTO() {}

    public IngredientDTO(Long id, String name, String supplierName, String insertedAt, String updatedAt) {
        this.id = id;
        this.name = name;
        this.supplierName = supplierName;
        this.insertedAt = insertedAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSupplierName() { return supplierName; }
    public void setSupplierName(String supplierName) { this.supplierName = supplierName; }

    public String getInsertedAt() { return insertedAt; }
    public void setInsertedAt(String insertedAt) { this.insertedAt = insertedAt; }

    public String getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }
}